package org.gristle.adventOfCode.y2021.d5

import org.gristle.adventOfCode.utilities.*

class Y2021D5(input: String) {

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

    fun part1() = solve(false)

    fun part2() = solve(true)

}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D5(readRawInput("y2021/d5"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 5774
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 18423
}