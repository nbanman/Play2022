package org.gristle.adventOfCode.y2021.d6

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts

class Y2021D6(input: String) : Day {

    private val fish = input
        .getInts()
        .groupingBy { it }
        .eachCount()

    // initialize count of fish by day
    private val fishByDays = List(9) { i ->
        fish[i]?.toLong() ?: 0L
    }

    // iterate one day
    private fun List<Long>.propagate() = List(9) { i ->
        when (i) {
            6 -> get(0) + get(7)
            8 -> get(0)
            else -> get(i + 1)
        }
    }

    fun solve(days: Int): Long = generateSequence(fishByDays) { it.propagate() }
        .take(days + 1)
        .last()
        .sum()

    override fun part1() = solve(80)

    override fun part2() = solve(256)
}

fun main() = Day.runDay(6, 2021, Y2021D6::class)

//    Class creation: 16ms
//    Part 1: 361169 (0ms)
//    Part 2: 1634946868992 (0ms)
//    Total time: 17ms