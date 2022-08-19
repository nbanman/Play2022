package org.gristle.adventOfCode.y2015.d4

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.md5
import org.gristle.adventOfCode.utilities.readRawInput

object Y2015D4 {
    private val input = readRawInput("y2015/d4")

    fun part1() = generateSequence(1) { it + 1 }.first { (input + it).md5().take(5) == "00000" }

    fun part2() = generateSequence(1) { it + 1 }.first { (input + it).md5().take(6) == "000000" }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D4.part1()} (${elapsedTime(time)}ms)") // 117946
    time = System.nanoTime()
    println("Part 2: ${Y2015D4.part2()} (${elapsedTime(time)}ms)") // 3938038
}