package org.gristle.adventOfCode.y2017.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import org.gristle.adventOfCode.utilities.getPairs

class Y2017D2(input: String) : Day {

    private val spreadsheet = input
        .lineSequence()
        .map { it.getIntList() }
        .toList()

    override fun part1() = spreadsheet
        .sumOf { (it.maxOrNull()?.minus(it.minOrNull() ?: 0) ?: 0) }

    override fun part2() = spreadsheet.sumOf { row ->
        row.getPairs().sumOf { combo ->
            val (lesser, greater) = if (combo[0] < combo[1]) combo[0] to combo[1] else combo[1] to combo[0]
            if (greater % lesser == 0) greater / lesser else 0
        }
    }
}

fun main() = Day.runDay(2, 2017, Y2017D2::class)

//    Class creation: 19ms
//    Part 1: 45972 (0ms)
//    Part 2: 326 (2ms)
//    Total time: 21ms
