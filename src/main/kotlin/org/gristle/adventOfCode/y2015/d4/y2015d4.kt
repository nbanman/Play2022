package org.gristle.adventOfCode.y2015.d4

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.md5
import org.gristle.adventOfCode.utilities.readRawInput

object Y2015D4 {
    private val input = readRawInput("y2015/d4")

    fun solve(digitLength: Int) = generateSequence(1) { it + 1 }
        .first { i -> (input + i).md5().take(digitLength).all { it == '0' } }

    fun part1() = solve(5)

    fun part2() = solve(6)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D4.part1()} (${elapsedTime(time)}ms)") // 117946
    time = System.nanoTime()
    println("Part 2: ${Y2015D4.part2()} (${elapsedTime(time)}ms)") // 3938038
}