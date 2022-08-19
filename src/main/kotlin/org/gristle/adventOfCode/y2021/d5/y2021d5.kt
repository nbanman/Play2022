package org.gristle.adventOfCode.y2021.d5

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

object Y2021D5 {
    private val input = readRawInput("y2021/d5")

    private val pattern = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()

    data class Line(val start: Coord, val end: Coord) {
        fun straightRange(includeDiagonals: Boolean = false): List<Coord> {
            return if (start.x == end.x) {
                val (small, large) = if (start.y < end.y) start.y to end.y else end.y to start.y
                (small..large).fold(emptyList()) { acc, i ->
                    acc + Coord(start.x, i)
                }
            } else if (start.y == end.y) {
                val (small, large) = if (start.x < end.x) start.x to end.x else end.x to start.x
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

    }

    val lines = input
        .groupValues(pattern) { it.toInt() }
        .map { Line(Coord(it[0], it[1]), Coord(it[2], it[3])) }

    fun part1(): Int {
        val space = mutableMapOf<Coord, Int>()
        lines.flatMap { it.straightRange() }.forEach {
            space[it] = (space[it] ?: 0) + 1
        }
        return space.count { it.value >= 2 }
    }

    fun part2(): Int {
        val space = mutableMapOf<Coord, Int>()
        lines.flatMap { it.straightRange(true) }.forEach {
            space[it] = (space[it] ?: 0) + 1
        }
        return space.count { it.value >= 2 }
    }

}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D5.part1()} (${elapsedTime(time)}ms)") // 5774
    time = System.nanoTime()
    println("Part 2: ${Y2021D5.part2()} (${elapsedTime(time)}ms)") // 18423
}