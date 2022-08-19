package org.gristle.adventOfCode.y2016.d25

import org.gristle.adventOfCode.utilities.elapsedTime

object Y2016D25 {
    // Reverse engineering the code ends with infinite loop that prints binary representation of d over and
    // over again, and defines d = a + 2555. So a = d - 2555. Starting from 2555, the first number that is
    // 1010..10 is 2730. So that's d.
    fun part1() = 2730 - 2555
}

fun main() {
    val time = System.nanoTime()
    println("Part 1: ${Y2016D25.part1()} (${elapsedTime(time)}ms)") // 175
}