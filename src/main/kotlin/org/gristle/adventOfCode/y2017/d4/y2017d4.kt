package org.gristle.adventOfCode.y2017.d4

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readInput

object Y2017D4 {
    private val input = readInput("y2017/d4")

    private val passphrases = input.map { it.split(' ') }

    fun part1() = passphrases.count { it.size == it.distinct().size }

    fun part2() = passphrases
        .map { phrase -> phrase.map { it.toCharArray().sorted() } }
        .count { it.size == it.distinct().size }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D4.part1()} (${elapsedTime(time)}ms)") // 455
    time = System.nanoTime()
    println("Part 2: ${Y2017D4.part2()} (${elapsedTime(time)}ms)") // 186
}