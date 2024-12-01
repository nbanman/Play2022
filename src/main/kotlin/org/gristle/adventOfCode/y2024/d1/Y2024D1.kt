package org.gristle.adventOfCode.y2024.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.collate
import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.getIntList
import kotlin.math.abs

class Y2024D1(val input: String) : Day {

    override fun part1() = input
        .getIntList()
        .collate(2)
        .map { it.sorted() }
        .let { (a, b) -> a zip b }
        .sumOf { (a, b) -> abs(a - b) }

    override fun part2(): Int {
        val (a, b) = input.getIntList().collate(2)
        val freq = b.eachCount()
        return a.sumOf { it * (freq[it] ?: 0) }
    }
}

fun main() = Day.runDay(Y2024D1::class)

//    Class creation: 1ms
//    Part 1: 1222801 (11ms)
//    Part 2: 22545250 (6ms)
//    Total time: 20ms