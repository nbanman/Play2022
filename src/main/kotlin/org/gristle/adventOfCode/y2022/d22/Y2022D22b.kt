package org.gristle.adventOfCode.y2022.d22

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.*
import kotlin.math.sqrt

private typealias DirectionChange2 = Pair<Nsew, Nsew>
private typealias CoordDirection2 = Pair<Coord, Nsew>

class Y2022D22b(input: String) : Day {

    enum class SixAxis {
        U {
            override fun turn(nsew: Nsew): Pair<SixAxis, Int> = when (nsew) {
                Nsew.NORTH -> N to 2
                Nsew.SOUTH -> S to 2
                Nsew.EAST -> E to 2
                Nsew.WEST -> W to 2
            }
        },
        D {
            override fun turn(nsew: Nsew): Pair<SixAxis, Int> = when (nsew) {
                Nsew.NORTH -> N to 0
                Nsew.SOUTH -> S to 2
                Nsew.EAST -> W to 3
                Nsew.WEST -> E to 1
            }
        },
        N {
            override fun turn(nsew: Nsew): Pair<SixAxis, Int> = when (nsew) {
                Nsew.NORTH -> U to 2
                Nsew.SOUTH -> D to 0
                Nsew.EAST -> W to 0
                Nsew.WEST -> E to 0
            }
        },
        S {
            override fun turn(nsew: Nsew): Pair<SixAxis, Int> = when (nsew) {
                Nsew.NORTH -> U to 0
                Nsew.SOUTH -> D to 2
                Nsew.EAST -> E to 0
                Nsew.WEST -> W to 0
            }
        },
        E {
            override fun turn(nsew: Nsew): Pair<SixAxis, Int> = when (nsew) {
                Nsew.NORTH -> U to 3
                Nsew.SOUTH -> D to 3
                Nsew.EAST -> N to 0
                Nsew.WEST -> S to 0
            }
        },
        W {
            override fun turn(nsew: Nsew): Pair<SixAxis, Int> = when (nsew) {
                Nsew.NORTH -> U to 1
                Nsew.SOUTH -> D to 1
                Nsew.EAST -> S to 0
                Nsew.WEST -> N to 0
            }
        };

        abstract fun turn(nsew: Nsew): Pair<SixAxis, Int>
    }

    class Face(val orientation: SixAxis, val grid: Grid<Char>)

    companion object {
        private fun Nsew.facing() = when (this) {
            Nsew.NORTH -> 3
            Nsew.SOUTH -> 1
            Nsew.EAST -> 0
            Nsew.WEST -> 2
        }

        private fun List<List<Char>>.getBounds() = map { line ->
            line.indexOfFirst { it != ' ' }..line.indexOfLast { it != ' ' }
        }
    }

    enum class Command { LEFT, RIGHT, FORWARD }

    private val lines = input.lines()

    private val grove = lines.dropLast(2).let { groveLines ->
        val width = groveLines.maxOf(String::length)
        val height = groveLines.size
        val rows = groveLines.map { it.padEnd(width) } // it will all come to a padEnd
        Grid(width, height) { i ->
            val y = i / width
            val x = i % width
            rows[y][x]
        }
    }

    private val path = buildList {
        Regex("""\d+|[LR]""")
            .findAll(lines.last())
            .map(MatchResult::value)
            .forEach {
                when {
                    it[0] == 'L' -> add(Command.LEFT)
                    it[0] == 'R' -> add(Command.RIGHT)
                    else -> repeat(it.toInt()) { add(Command.FORWARD) }
                }
            }
    }

    /**
     * Main loop is a fold that traverses the path. It delegates the translation of commands to a "move" function
     * which is supplied by the two parts of the puzzle. Return is the scoring for the final position.
     */
    fun solve(move: (CoordDirection2) -> CoordDirection2): Int {
        val start = Coord.fromIndex(grove.indexOfFirst { it == '.' }, grove.width)
        var dir = Nsew.EAST
        val end = path.fold(start) { pos, command ->
            if (command == Command.FORWARD) {
                val (prospect, prospectiveDir) = move(CoordDirection2(pos, dir))
                if (grove[prospect] == '#') {
                    pos
                } else {
                    dir = prospectiveDir
                    prospect
                }
            } else {
                dir = when (command) {
                    Command.LEFT -> dir.left()
                    Command.RIGHT -> dir.right()
                    else -> throw IllegalArgumentException("Should only be turning.")
                }
                pos
            }
        }
        return 1000 * (end.y + 1) + 4 * (end.x + 1) + dir.facing()
    }

    /**
     * Part 1 traversal wraps the area. It does this by tracking the bounds of each row and column and makes any value
     * higher than the upper bound become the lower bound and vice-versa.
     */
    override fun part1(): Int {
        val rowBounds = grove.rows().getBounds()
        val colBounds = grove.columns().getBounds()

        val move: (CoordDirection2) -> Pair<Coord, Nsew> = { (pos, dir): CoordDirection2 ->
            val prospect = pos.move(dir)
            if (!grove.validCoord(prospect) || grove[prospect] == ' ') {
                if (dir == Nsew.NORTH || dir == Nsew.SOUTH) { // north or south
                    val newY = (if (prospect.y < colBounds[prospect.x].first) Int.MAX_VALUE else Int.MIN_VALUE)
                        .coerceIn(colBounds[prospect.x])
                    prospect.copy(y = newY) to dir
                } else { // east or went
                    val newX = (if (prospect.x < rowBounds[prospect.y].first) Int.MAX_VALUE else Int.MIN_VALUE)
                        .coerceIn(rowBounds[prospect.y])
                    prospect.copy(x = newX) to dir
                }
            } else prospect to dir
        }
        return solve(move)
    }

    /**
     * Part 2 traversal uses cube geometry. It makes a "miniGrove" which just has one pixel per side. Then it runs
     * BFS from every point to every point on the miniGrove. The path of each resulting destination point is a shape.
     * Shapes get folded the same way every time. I keep a list of sets of points along with their corresponding
     * direction changes. To avoid duplication these shapes all start at 0,0 and continue to 0,1. So the shapes found
     * by BFS need to be rotated and flipped to match that orientation, with corresponding changes to the direction
     * changes.
     */

    override fun part2(): Int {
        // get the length of each side
        val sideLength = sqrt((grove.size - grove.count { it == ' ' }) / 6.0).toInt()

        // make mini-grid, one pixel per side
        val miniWidth = grove.width / sideLength
        val miniGrove = buildList {
            for (y in 0 until grove.height / sideLength) for (x in 0 until miniWidth) {
                add(grove[Coord(x * sideLength, y * sideLength)] != ' ')
            }
        }.toGrid(miniWidth)

        fun Graph.StdVertex<Pair<SixAxis, Coord>>.toFace(): Face {
            val (face, pos) = this.id
            val subGrid = grove.subGrid(pos * Coord(sideLength, sideLength), sideLength, sideLength)
            if (parent == null)
                return Face(SixAxis.U, subGrid)
            val direction = when (pos - parent.id.second) {
                Coord(1, 0) -> Nsew.EAST
                Coord(-1, 0) -> Nsew.WEST
                Coord(0, 1) -> Nsew.SOUTH
                Coord(0, -1) -> Nsew.NORTH
                else -> throw IllegalArgumentException("invalid coordinate created")
            }
            val (newFace, orientInt) = face.turn(direction)
            val newGrid = when (orientInt) {
                0 -> subGrid
                1 -> subGrid.rotate90()
                2 -> subGrid.rotate180()
                3 -> subGrid.rotate270()
                else -> throw IllegalArgumentException("invalid orientInt: $orientInt")
            }
            return Face(newFace, newGrid)
        }

        val pos = miniGrove.coordOf(miniGrove.indexOfFirst { it })
        val firstFace = let {
            val subGrid = grove.subGrid(pos * Coord(sideLength, sideLength), sideLength, sideLength)
            Face(SixAxis.U, grove.subGrid(pos * Coord(sideLength, sideLength), sideLength, sideLength))
        }
        /*
                val faces = Graph
                    .bfs(pos to firstFace) { face ->
                        pos
                            .getNeighbors()
                            .filter { miniGrove.validCoord(it) && miniGrove[it] }
                            .map { neighbor ->
                                
                            }
                    }
        */


        return 0
    }
}

fun main() = Day.runDay(Y2022D22b::class)