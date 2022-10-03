package org.gristle.adventOfCode.y2016.d25

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2016D25(input: String) {
    // Reverse engineering the code ends with infinite loop that prints binary representation of d over and
    // over again, and defines d = a + 2555. So a = d - 2555. Starting from 2555, the first number that is
    // 1010..10 is 2730. So that's d.
    fun part1() = 2730 - 2555
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D25(readRawInput("y2016/d25"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 175
}