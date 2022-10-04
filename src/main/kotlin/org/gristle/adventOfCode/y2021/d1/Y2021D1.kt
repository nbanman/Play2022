package org.gristle.adventOfCode.y2021.d1

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2021D1(input: String) {

    private val measurements = input.lines().map { it.toInt() }

    private fun List<Int>.countIncreased() = windowed(2).count { it.last() > it.first() }

    fun part1() = measurements.countIncreased()

    fun part2() = measurements
        .windowed(3)
        .map { it.sum() }
        .countIncreased()
}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D1(readRawInput("y2021/d1"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1342
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1378
}