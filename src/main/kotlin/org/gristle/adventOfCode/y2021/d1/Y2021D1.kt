package org.gristle.adventOfCode.y2021.d1

import org.gristle.adventOfCode.Day

class Y2021D1(input: String) : Day {
    private val measurements = input.lines().map(String::toInt)

    private fun List<Int>.countIncreased() = zipWithNext().count { (a, b) -> a < b }

    override fun part1() = measurements.countIncreased()

    override fun part2() = measurements
        .windowed(3) { it.sum() }
        .countIncreased()
}

fun main() = Day.runDay(Y2021D1::class)

//    Class creation: 30ms
//    Part 1: 1342 (0ms)
//    Part 2: 1378 (5ms)
//    Total time: 37ms