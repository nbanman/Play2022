package org.gristle.adventOfCode.y2021.d1

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.readRawInput

class Y2021D1(input: String) {
    private val measurements = input.lines().map(String::toInt)

    private fun List<Int>.countIncreased() = zipWithNext().count { (a, b) -> a < b }

    fun part1() = measurements.countIncreased()

    fun part2() = measurements
        .windowed(3) { it.sum() }
        .countIncreased()
}

fun main() {
    val timer = Stopwatch()
    val c = Y2021D1(readRawInput("y2021/d1"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 1342
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 1378
}