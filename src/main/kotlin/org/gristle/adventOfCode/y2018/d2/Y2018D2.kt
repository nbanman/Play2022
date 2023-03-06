package org.gristle.adventOfCode.y2018.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.getPairs

class Y2018D2(input: String) : Day {
    private val boxIds = input.lines()
    override fun part1(): Int {
        val frequencies = boxIds.map { it.eachCount().values }
        return frequencies.count { it.contains(2) } * frequencies.count { it.contains(3) }
    }

    override fun part2(): String {
        fun List<String>.countDifferences() = first()
            .zip(last())
            .count { (first, last) -> first != last }

        fun List<String>.shared() = first()
            .zip(last())
            .filter { (first, last) -> first == last }
            .map { (first, _) -> first }
            .joinToString("")

        return boxIds.getPairs().first { it.countDifferences() == 1 }.shared()
    }
}

fun main() = Day.runDay(2, 2018, Y2018D2::class)

//    Class creation: 20ms
//    Part 1: 7688 (7ms)
//    Part 2: lsrivmotzbdxpkxnaqmuwcchj (40ms)
//    Total time: 68ms