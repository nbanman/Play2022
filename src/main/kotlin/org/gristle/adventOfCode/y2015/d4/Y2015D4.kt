package org.gristle.adventOfCode.y2015.d4

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.md5
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D4(private val input: String) {
    fun solve(digitLength: Int) = generateSequence(1) { it + 1 }
        .first { i -> (input + i).md5().take(digitLength).all { it == '0' } }

    fun part1() = solve(5)

    fun part2() = solve(6)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D4(readRawInput("y2015/d4"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 117946
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 3938038
}