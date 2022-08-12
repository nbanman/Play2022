package org.gristle.adventOfCode.y2021.d1

import org.gristle.adventOfCode.utilities.*

object Y2021D1 {
    private val input = readInput("y2021/d1")

    val measurements = input.map { it.toInt() }

    private fun List<Int>.countIncreased() = windowed(2).count { it.last() > it.first() }

    fun part1() = measurements.countIncreased()

    fun part2() = measurements
        .windowed(3)
        .map { it.sum() }
        .countIncreased()
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D1.part1()} (${elapsedTime(time)}ms)") // 1342
    time = System.nanoTime()
    println("Part 2: ${Y2021D1.part2()} (${elapsedTime(time)}ms)") // 1378
}