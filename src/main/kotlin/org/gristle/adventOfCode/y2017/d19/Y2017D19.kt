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
            val neighbors = Nsew
                .entries
                .mapNotNull { dir -> maze.moveOrNull(index, dir)?.let { it to dir } }
                .filter { (neighborIndex, _) -> maze[neighborIndex] != ' ' }

            if (neighbors.size == 1 && index != startIndex) return newReport to steps + 1
            
            if (spot == '+') {
                if (direction.ordinal < 2) {
                    val (newIndex, newDirection) = neighbors.first { (_, dir) -> dir.ordinal > 1 }
                    return runMaze(newIndex, newDirection, newReport, steps + 1)
                } else {
                    val (newIndex, newDirection) = neighbors.first { (_, dir) -> dir.ordinal < 2 }
                    return runMaze(newIndex, newDirection, newReport, steps + 1)
                }
            } else {
                val (newIndex, newDirection) = neighbors.first { (_, dir) -> dir == direction }
                return runMaze(newIndex, newDirection, newReport, steps + 1)
            }
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