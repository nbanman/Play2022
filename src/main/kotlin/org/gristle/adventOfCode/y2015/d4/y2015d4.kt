package org.gristle.adventOfCode.y2015.d4

import org.gristle.adventOfCode.utilities.*

object Y2015D4 {
    private const val input = "ckczppom"

    fun part1() = generateSequence(1) { it + 1 }.first { (input + it).md5().take(5) == "00000" }

    fun part2() = generateSequence(1) { it + 1 }.first { (input + it).md5().take(6) == "000000" }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D4.part1()} (${elapsedTime(time)}ms)") // 117946
    time = System.nanoTime()
    println("Part 2: ${Y2015D4.part2()} (${elapsedTime(time)}ms)") // 3938038
}