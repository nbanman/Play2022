package org.gristle.adventOfCode.y2017.d19

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.StringGrid

class Y2017D19(private val maze: String) : Day {
    class Mouse(private val maze: StringGrid, private val startIndex: Int) {

        tailrec fun runMaze(
            index: Int = startIndex,
            direction: Nsew = Nsew.SOUTH,
            report: String = "",
            steps: Int = 0
        ): Pair<String, Int> {
            val spot = maze[index]
            val newReport = report + if (spot.isLetter()) spot else ""
            val neighborIndices = maze
                .getNeighborIndices(index)
                .filter { maze[it] != ' ' }

            if (neighborIndices.size == 1 && index != startIndex) return newReport to steps + 1

            val newIndex = if (spot == '+') {
                if (direction == Nsew.NORTH || direction == Nsew.SOUTH) {
                    if ((index - 1) in neighborIndices) index - 1 else index + 1
                } else { // location is left or right
                    if ((index - maze.width) in neighborIndices) index - maze.width else index + maze.width
                }
            } else {
                when (direction) {
                    Nsew.NORTH -> index - maze.width
                    Nsew.SOUTH -> index + maze.width
                    Nsew.EAST -> index + 1
                    Nsew.WEST -> index - 1
                }
            }
            val newDirection = when (newIndex) {
                index - 1 -> Nsew.WEST
                index + 1 -> Nsew.EAST
                index + maze.width -> Nsew.SOUTH
                else -> Nsew.NORTH
            }
            return runMaze(newIndex, newDirection, newReport, steps + 1)
        }
    }

    val solution: Pair<String, Int> by lazy {
        val startIndex = maze.indexOfFirst { it != ' ' }
        Mouse(StringGrid(maze), startIndex).runMaze()
    }

    override fun part1() = solution.first

    override fun part2() = solution.second
}

fun main() = Day.runDay(Y2017D19::class)

//    Class creation: 2ms
//    Part 1: EOCZQMURF (56ms)
//    Part 2: 16312 (0ms)
//    Total time: 58ms