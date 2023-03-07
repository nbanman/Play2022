package org.gristle.adventOfCode.y2016.d25

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts

class Y2016D25(input: String) : Day {

    private val offset = input.getInts().first()

    private fun Int.isClockSignal() = generateSequence(this) { it / 2 }
        .takeWhile { it != 0 }
        .withIndex()
        .all { (idx, value) -> idx and 1 == value and 1 }

    // Reverse engineering the code ends with infinite loop that prints reverse binary representation of d over and
    // over again, and defines d = a + 2555. So a = d - 2555. Starting from 2555, the first number that is
    // 10101010...10 is 2730. So that's d. A is 2555 less than that, or 175.    
    override fun part1() = generateSequence(1) { it + 1 }
        .first { (it + offset).isClockSignal() }

    override fun part2() = null
}

fun main() = Day.runDay(Y2016D25::class)

//    Class creation: 12ms
//    Part 1: 175 (1ms)
//    Total time: 14ms