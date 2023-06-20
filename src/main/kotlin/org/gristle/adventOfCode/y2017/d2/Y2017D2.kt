package org.gristle.adventOfCode.y2017.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getPairs
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.minMax

class Y2017D2(input: String) : Day {
    private val spreadsheet: List<List<Int>> = input.lines { it.split('\t').map(String::toInt) }

    private inline fun solve(lineOperation: (List<Int>) -> Int): Int = spreadsheet.sumOf(lineOperation)

    override fun part1() = solve { row -> row.minMax().let { (min, max) -> max - min } }

    override fun part2() = solve { row ->
        row.getPairs().sumOf {
            val (smaller, larger) = it.minMax()
            if (larger % smaller == 0) larger / smaller else 0
        }
    }
}

fun main() = Day.runDay(Y2017D2::class)

//    Class creation: 5ms
//    Part 1: 45972 (0ms)
//    Part 2: 326 (3ms)
//    Total time: 9ms