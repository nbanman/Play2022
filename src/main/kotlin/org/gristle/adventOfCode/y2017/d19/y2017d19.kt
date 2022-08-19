package org.gristle.adventOfCode.y2017.d19

import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

object Y2017D19 {
    class Mouse(private val maze: Grid<Char>, private val startIndex: Int) {

        tailrec fun runMaze(
            index: Int = startIndex,
            direction: String = "down",
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
                if (direction == "up" || direction == "down") {
                    if ((index - 1) in neighborIndices) index - 1 else index + 1
                } else { // location is left or right
                    if ((index - maze.width) in neighborIndices) index - maze.width else index + maze.width
                }
            } else {
                when (direction) {
                    "up" -> index - maze.width
                    "down" -> index + maze.width
                    "left" -> index - 1
                    else -> index + 1
                }
            }
            val newDirection = when(newIndex) {
                index - 1 -> "left"
                index + 1 -> "right"
                index + maze.width -> "down"
                else -> "up"
            }
            return runMaze(newIndex, newDirection, newReport, steps + 1)
        }
    }

    private val data = readRawInput("y2017/d19")

    fun solve(): Pair<String, Int> {
        val maze = data.toGrid()
        val startIndex = maze.indexOfFirst { it != ' ' }
        val (p1, p2) = Mouse(maze, startIndex).runMaze()
        return p1 to p2
    }
}

fun main() {
    val time = System.nanoTime()
    val (p1, p2) = Y2017D19.solve()
    println("Part 1: $p1") // EOCZQMURF
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 16312
}