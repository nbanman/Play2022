package org.gristle.adventOfCode.y2020.d1

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.getPairs
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D1(input: String) {

    private val entries = input.lines().map(String::toInt)
    private val entrySet = entries.toSet()

    fun part1() = entries // for each entry...
        .first { entrySet.contains(2020 - it) } // find the first one where there is another entry that adds to 2020 
        .let { it * (2020 - it) } // multiply the two together

    fun part2() = entries
        .getPairs() // convert entries into a list of paired combinations of entries
        // find first pair where there is another entry that adds to 2020
        .first { (first, second) -> entrySet.contains(2020 - first - second) }
        .let { (first, second) -> first * second * (2020 - first - second) } // multiply the three together
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D1(readRawInput("y2020/d1"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1015476
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 200878544
}