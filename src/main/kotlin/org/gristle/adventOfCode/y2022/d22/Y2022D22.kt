package org.gristle.adventOfCode.y2022.d22

import org.gristle.adventOfCode.utilities.*
import kotlin.math.sqrt

private typealias DirectionChange = Pair<Nsew, Nsew>
private typealias CoordDirection = Pair<Coord, Nsew>

class Y2022D22(input: String) {

    companion object {
        fun Nsew.facing() = when (this) {
            Nsew.NORTH -> 3
            Nsew.SOUTH -> 1
            Nsew.EAST -> 0
            Nsew.WEST -> 2
        }

        fun IntRange.span() = last - first + 1

        fun List<IntRange>.maxSpan() = maxOf { it.span() }

        fun List<List<Char>>.getBounds() = map { line ->
            line.indexOfFirst { it != ' ' }..line.indexOfLast { it != ' ' }
        }
    }

    enum class Command { LEFT, RIGHT, FORWARD }

    private val lines = input.lines()

    private var grove = lines.dropLast(2).let { groveLines ->
        val width = groveLines.maxOf(String::length)
        val height = groveLines.size
        val rows = groveLines.map { it.padEnd(width) } // it will all come to a padEnd
        List(height * width) { i ->
            val y = i / width
            val x = i % width
            rows[y][x]
        }.toMutableGrid(width) // todo make nonmutable once debugging over
    }

    private var rowBounds = grove.rows().getBounds()
    private var colBounds = grove.columns().getBounds()

    init {
        if (rowBounds.maxSpan() > colBounds.maxSpan()) {
            grove = grove.rotate90().toMutableGrid() // todo unMut
            rowBounds = grove.rows().getBounds()
            colBounds = grove.columns().getBounds()
        }
    }

    private val path = buildList {
        var dir = Nsew.EAST
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

    fun solve(move: (Coord, Nsew) -> Pair<Coord, Nsew>): Int {
        val start = Coord(rowBounds[0].first, 0)
        var dir = Nsew.EAST
        val end = path.foldIndexed(start) { index, pos, command ->
            if (command == Command.FORWARD) {
                val (prospect, prospectiveDir) = move(pos, dir)
                if (grove[prospect] == '#') {
                    pos
                } else {
                    dir = prospectiveDir
                    if (grove[prospect] == '.') grove[prospect] = when (dir) { // todo erase once debugged
                        Nsew.NORTH -> '^'
                        Nsew.SOUTH -> 'v'
                        Nsew.EAST -> '>'
                        Nsew.WEST -> '<'
                    }
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

    fun part1(): Int {
        val move = { pos: Coord, dir: Nsew ->
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

    fun part2(): Int {
        // get the length of each side
        val sideLength = sqrt((grove.size - grove.count { it == ' ' }) / 6.0).toInt()

        // make mini-grid, one pixel per side
        val miniWidth = grove.width / sideLength
        val miniGrove = buildList {
            for (y in 0 until grove.height / sideLength) for (x in 0 until miniWidth) {
                add(grove[Coord(x * sideLength, y * sideLength)])
            }
        }.toGrid(miniWidth)

        // map of various shapes

        val shapes = listOf(
//            listOf(0 to 0, 0 to 1) to DirectionChange(Nsew.SOUTH, Nsew.SOUTH),
            listOf(0 to 0, 0 to 1, 1 to 1) to DirectionChange(Nsew.EAST, Nsew.SOUTH),
            listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2) to DirectionChange(Nsew.EAST, Nsew.WEST),
            listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1) to DirectionChange(Nsew.NORTH, Nsew.SOUTH),
            listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3) to DirectionChange(Nsew.SOUTH, Nsew.SOUTH),
            listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3, 1 to 3) to DirectionChange(Nsew.WEST, Nsew.SOUTH),
            listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1, 3 to 1) to DirectionChange(Nsew.EAST, Nsew.EAST),
            listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 1 to 3) to DirectionChange(Nsew.NORTH, Nsew.WEST),
            listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2, 1 to 3) to DirectionChange(Nsew.WEST, Nsew.NORTH),
            listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2, 2 to 2) to DirectionChange(Nsew.SOUTH, Nsew.EAST),
            listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2, 2 to 2, 2 to 3) to DirectionChange(Nsew.EAST, Nsew.EAST),
            listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2, 1 to 3, 2 to 3) to DirectionChange(Nsew.NORTH, Nsew.NORTH),
            listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1, 2 to 2, 3 to 2) to DirectionChange(Nsew.WEST, Nsew.WEST),
            listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 1 to 3, 1 to 4) to DirectionChange(Nsew.EAST, Nsew.EAST),
        ).associate { (shape, directions) -> shape.map { it.toCoord() }.toSet() to directions }

        val sides: Map<Coord, Map<Nsew, CoordDirection>> = buildMap {
            miniGrove
                .indices
                .filter { miniGrove[it] != ' ' }
                .forEach { index ->
                    val start = Coord.fromIndex(index, miniWidth)
                    val vertices = Graph.bfs(start) { pos ->
                        miniGrove
                            .getNeighborsIndexedValue(pos)
                            .filter { it.value != ' ' }
                            .map { Coord.fromIndex(it.index, miniWidth) }
                    }.drop(1)

                    val huh: Map<Nsew, CoordDirection> = buildMap {
                        for (vertex in vertices) {
                            val coords = vertex.path().map { it.id - start }
                            val rotate: (List<Coord>) -> List<Coord> = when (coords[1]) {
                                Coord(0, 1) -> { shape: List<Coord> -> shape }
                                Coord(0, -1) -> { shape: List<Coord> -> shape.rotate180() }
                                Coord(1, 0) -> { shape: List<Coord> -> shape.rotate90() }
                                else -> { shape: List<Coord> -> shape.rotate270() }
                            }
                            var rotated = rotate(coords).toSet()
                            val flipped = rotated.last().x < 0
                            if (flipped) rotated = rotated.flipY().toSet()
                            val directions = shapes[rotated]
                            if (directions == null) continue
                            val reverse = when (coords[1]) {
                                Coord(0, 1) -> { dir: Nsew -> dir }
                                Coord(0, -1) -> { dir: Nsew -> dir.flip() }
                                Coord(1, 0) -> { dir: Nsew -> dir.left() }
                                else -> { dir: Nsew -> dir.right() }
                            }

                            fun Nsew.flipY() = when {
                                this == Nsew.NORTH || this == Nsew.SOUTH -> this
                                else -> this.flip()
                            }
                            if (flipped) {
                                put(reverse(directions.first.flipY()), vertex.id to reverse(directions.second.flipY()))
                            } else {
                                put(reverse(directions.first), vertex.id to reverse(directions.second))
                            }
                        }
                    }
                    put(start, huh)
                }
        }

        val move = { pos: Coord, dir: Nsew ->
            val prospect = pos.move(dir)
            if (!grove.validCoord(prospect) || grove[prospect] == ' ') {
                val side = Coord(pos.x / sideLength, pos.y / sideLength)
                val (newSide, newDir) = sides.getValue(side).getValue(dir)
                val bigNewSide = Coord(newSide.x * sideLength, newSide.y * sideLength)
                val relativeCoord = Coord(pos.x % sideLength, pos.y % sideLength)
                when (newDir) {
                    Nsew.NORTH -> {
                        val y = bigNewSide.y - 1 + sideLength
                        val x = when (dir) {
                            Nsew.SOUTH -> bigNewSide.x + sideLength - relativeCoord.x - 1
                            Nsew.EAST -> bigNewSide.x + relativeCoord.y
                            Nsew.WEST -> bigNewSide.x + sideLength - relativeCoord.y - 1
                            else -> bigNewSide.x + relativeCoord.x
                        }
                        println(
                            "$pos [$side] moved $dir, to $prospect , becomes ${
                                Coord(
                                    x,
                                    y
                                )
                            } [$newSide] going $newDir"
                        )
                        Coord(x, y) to newDir
                    }

                    Nsew.SOUTH -> {
                        val y = bigNewSide.y
                        val x = when (dir) {
                            Nsew.NORTH -> bigNewSide.x + sideLength - relativeCoord.x - 1
                            Nsew.WEST -> bigNewSide.x + relativeCoord.y
                            Nsew.EAST -> bigNewSide.x + sideLength - relativeCoord.y - 1
                            Nsew.SOUTH -> bigNewSide.x + relativeCoord.x
                        }
                        println(
                            "$pos [$side] moved $dir, to $prospect , becomes ${
                                Coord(
                                    x,
                                    y
                                )
                            } [$newSide] going $newDir"
                        )
                        Coord(x, y) to newDir
                    }

                    Nsew.EAST -> {
                        val x = bigNewSide.x
                        val y = when (dir) {
                            Nsew.NORTH -> bigNewSide.y + relativeCoord.x
                            Nsew.SOUTH -> bigNewSide.y + sideLength - relativeCoord.x - 1
                            Nsew.WEST -> bigNewSide.y + sideLength - relativeCoord.y - 1
                            Nsew.EAST -> bigNewSide.y + relativeCoord.y
                        }
                        println(
                            "$pos [$side] moved $dir, to $prospect , becomes ${
                                Coord(
                                    x,
                                    y
                                )
                            } [$newSide] going $newDir"
                        )
                        Coord(x, y) to newDir
                    }

                    Nsew.WEST -> {
                        val x = bigNewSide.x - 1 + sideLength
                        val y = when (dir) {
                            Nsew.NORTH -> bigNewSide.y + sideLength - relativeCoord.x - 1
                            Nsew.SOUTH -> bigNewSide.y + relativeCoord.x
                            Nsew.EAST -> bigNewSide.y + sideLength - relativeCoord.y - 1
                            Nsew.WEST -> bigNewSide.y + relativeCoord.y
                        }
                        println(
                            "$pos [$side] moved $dir, to $prospect , becomes ${
                                Coord(
                                    x,
                                    y
                                )
                            } [$newSide] going $newDir"
                        )
                        Coord(x, y) to newDir
                    }
                }


            } else prospect to dir
        }
        return solve(move)
    }
}

fun main() {
    val input = listOf(
        getInput(22, 2022),
        """        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5""",
        """        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10L5L5L10R4L5R5""",
        """..
          | ...
          |   .
          |    
            |    10L5L5L10R4L5R5
        """.trimMargin()
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D22(input[0])
    println("Class creation: ${timer.lap()}ms")
//    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 133174 (30ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 190131 (too high!)
    println("Total time: ${timer.elapsed()}ms")
}