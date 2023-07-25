package org.gristle.adventOfCode.y2020.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import org.gristle.adventOfCode.utilities.getPairSequence

class Y2020D1(input: String) : Day {

    private val entries = input.getIntList()
    private val entrySet = entries.toSet()

    override fun part1() = entrySet // for each entry...
        .first { entrySet.contains(2020 - it) } // find the first one where there is another entry that adds to 2020 
        .let { it * (2020 - it) } // multiply the two together

    override fun part2() = entries
        .getPairSequence() // convert entries into a sequence of paired combinations of entries
        // find first pair where there is another entry that adds to 2020
        .first { (first, second) -> entrySet.contains(2020 - first - second) }
        .let { (first, second) -> first * second * (2020 - first - second) } // multiply the three together
}

fun main() = Day.runDay(Y2020D1::class)

//    Class creation: 6ms
//    Part 1: 1015476 (0ms)
//    Part 2: 200878544 (4ms)
//    Total time: 11ms