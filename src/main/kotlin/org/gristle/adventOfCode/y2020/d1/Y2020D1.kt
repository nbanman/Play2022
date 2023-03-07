package org.gristle.adventOfCode.y2020.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getPairs

class Y2020D1(input: String) : Day {

    private val entries = input.lines().map(String::toInt)
    private val entrySet = entries.toSet()

    override fun part1() = entries // for each entry...
        .first { entrySet.contains(2020 - it) } // find the first one where there is another entry that adds to 2020 
        .let { it * (2020 - it) } // multiply the two together

    override fun part2() = entries
        .getPairs() // convert entries into a list of paired combinations of entries
        // find first pair where there is another entry that adds to 2020
        .first { (first, second) -> entrySet.contains(2020 - first - second) }
        .let { (first, second) -> first * second * (2020 - first - second) } // multiply the three together
}

fun main() = Day.runDay(Y2020D1::class)

//    Class creation: 22ms
//    Part 1: 1015476 (0ms)
//    Part 2: 200878544 (6ms)
//    Total time: 28ms