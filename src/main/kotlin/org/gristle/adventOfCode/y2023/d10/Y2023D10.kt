package org.gristle.adventOfCode.y2023.d10

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.takeUntil
import org.gristle.adventOfCode.utilities.toGrid
import kotlin.math.absoluteValue

class Y2023D10(input: String) : Day {

    // convert String to Grid for easy traversal/lookup
    private val field = input.toGrid()

    // list of directions taken by the loop
    private val loop: List<Nsew> by lazy {
        
        // start at 'S'
        val startPos = field.coordOfElement('S')
        
        // find the initial direction by looking in each direction, and pick the first one that has a pipe
        // fitting that connects back to the start
        val startDir = Nsew.entries // get directions
            .first { direction -> // pick the first direction pointing to a fitting pointing back to startPos
                val neighborPos = startPos.move(direction)
                field.validCoord(neighborPos)
                        && movements.containsKey(field[neighborPos] to direction.flip())
            }
        
        // lambda for moving along the pipe, taking in the position and direction and returning the next
        // position and direction
        val move: (Pair<Coord, Nsew>) -> Pair<Coord, Nsew> = { (pos, dir) ->
            val newPos = pos.move(dir)
            val newDir = movements.getValue(field[newPos] to dir)
            newPos to newDir
        }
        
        // start at the position and direction *after* 'S', then continue until we hit 'S'. This gives a list
        // of all the fittings in the loop.
        generateSequence(move(startPos to startDir), move)
            .takeUntil { (pos, _) -> field[pos] == 'S' }
            .map { (_, dir) -> dir }
            .toList()
    }

    // Furthest direction is half-way along the loop.
    override fun part1() = loop.size / 2

    // Get the area of the loop, then get the inner points' area using Pick's theorem.
    // Area algorithm based on Stokes' theorem. h/t Jakub Gwóźdź
    override fun part2(): Int {
        val area = loop
            .fold(0 to 0) { (sum, d), dir ->
                when (dir) {
                    Nsew.NORTH -> sum to d - 1
                    Nsew.SOUTH -> sum to d + 1
                    Nsew.WEST -> sum - d to d
                    Nsew.EAST -> sum + d to d
                }
            }.first.absoluteValue
        
        // Pick's theorem: A = i + (b / 2]) + 1
        // Solve for i -> i = A - (b / 2) - 1
        return area - (loop.size / 2) + 1
    }

    companion object {
        
        private val movements: Map<Pair<Char, Nsew>, Nsew> = mapOf(
            ('S' to Nsew.NORTH) to Nsew.NORTH,
            ('S' to Nsew.SOUTH) to Nsew.SOUTH,
            ('S' to Nsew.EAST) to Nsew.EAST,
            ('S' to Nsew.WEST) to Nsew.WEST,
            ('|' to Nsew.NORTH) to Nsew.NORTH,
            ('|' to Nsew.SOUTH) to Nsew.SOUTH,
            ('-' to Nsew.EAST) to Nsew.EAST,
            ('-' to Nsew.WEST) to Nsew.WEST,
            ('L' to Nsew.SOUTH) to Nsew.EAST,
            ('L' to Nsew.WEST) to Nsew.NORTH,
            ('J' to Nsew.SOUTH) to Nsew.WEST,
            ('J' to Nsew.EAST) to Nsew.NORTH,
            ('7' to Nsew.EAST) to Nsew.SOUTH,
            ('7' to Nsew.NORTH) to Nsew.WEST,
            ('F' to Nsew.WEST) to Nsew.SOUTH,
            ('F' to Nsew.NORTH) to Nsew.EAST,
        )
    }
}

fun main() = Day.runDay(Y2023D10::class)

//    Class creation: 13ms
//    Part 1: 7086 (27ms)
//    Part 2: 317 (4ms)
//    Total time: 45ms

@Suppress("unused")
private val sampleInput = listOf(
    """.....
.S-7.
.|.|.
.L-J.
.....
""", """..F7.
.FJ|.
SJ.L7
|F--J
LJ...
""", """...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........
""", """..........
.S------7.
.|F----7|.
.||....||.
.||....||.
.|L-7F-J|.
.|..||..|.
.L--JL--J.
..........
""",""".F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...
""", """FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
""", 
)