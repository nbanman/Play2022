package org.gristle.adventOfCode.y2021.d5

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.minMax
import org.gristle.adventOfCode.utilities.parseToList

class Y2021D5(input: String) : Day {

    private val pattern = """(\d+),(\d+) -> (\d+),(\d+)"""

    data class Line(val start: Coord, val end: Coord) {
        fun straightRange(includeDiagonals: Boolean = false): List<Coord> =
            if (start.x == end.x) {
                val (small, large) = minMax(start.y, end.y)
                (small..large).fold(emptyList()) { acc, i ->
                    acc + Coord(start.x, i)
                }
            } else if (start.y == end.y) {
                val (small, large) = minMax(start.x, end.x)
                (small..large).fold(emptyList()) { acc, i ->
                    acc + Coord(i, start.y)
                }
            } else if (includeDiagonals) {
                val xRange = if (start.x < end.x) {
                    start.x..end.x
                } else {
                    start.x downTo end.x
                }

                val yIncrement = if (start.y < end.y) 1 else -1

                xRange.foldIndexed(emptyList()) { index, acc, i ->
                    acc + Coord(i, start.y + index * yIncrement)
                }
            } else {
                emptyList()
            }
    }

    val lines = input.parseToList(::Line, pattern)

    fun solve(includeDiagonals: Boolean): Int {
        val space = mutableMapOf<Coord, Int>()
        lines.flatMap { it.straightRange(includeDiagonals) }.forEach {
            space[it] = (space[it] ?: 0) + 1
        }
        return space.count { it.value >= 2 }
    }

    override fun part1() = solve(false)

    override fun part2() = solve(true)

}

fun main() = Day.runDay(Y2021D5::class)

//    Class creation: 78ms
//    Part 1: 5774 (154ms)
//    Part 2: 18423 (148ms)
//    Total time: 381ms   