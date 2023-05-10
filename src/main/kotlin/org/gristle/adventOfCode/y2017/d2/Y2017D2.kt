package org.gristle.adventOfCode.y2017.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import org.gristle.adventOfCode.utilities.getPairs
import org.gristle.adventOfCode.utilities.minMax

class Y2017D2(input: String) : Day {

    private val spreadsheet = input
        .lineSequence()
        .map(String::getIntList)
        .toList()

    override fun part1() = spreadsheet
        .sumOf { it.max() - it.min() }

    override fun part2() = spreadsheet.sumOf { row ->
        row.getPairs().sumOf { combo ->
            val (lesser, greater) = combo.minMax()
            if (greater % lesser == 0) greater / lesser else 0
        }
    }
}

fun main() = Day.runDay(Y2017D2::class)

//    Class creation: 19ms
//    Part 1: 45972 (0ms)
//    Part 2: 326 (2ms)
//    Total time: 21ms
