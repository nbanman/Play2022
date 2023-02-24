package org.gristle.adventOfCode.y2016.d25

import org.gristle.adventOfCode.Day

class Y2016D25(input: String) : Day {
    // Reverse engineering the code ends with infinite loop that prints binary representation of d over and
    // over again, and defines d = a + 2555. So a = d - 2555. Starting from 2555, the first number that is
    // 1010..10 is 2730. So that's d.
    override fun part1() = 2730 - 2555

    override fun part2() = null
}

fun main() = Day.runDay(25, 2016, Y2016D25::class)