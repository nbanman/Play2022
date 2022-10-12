package org.gristle.adventOfCode.y2017.d4

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D4(input: String) {

    private val passphrases = input.lines().map { it.split(' ') }

    fun part1() = passphrases.count { it.size == it.distinct().size }

    fun part2() = passphrases
        .map { phrase -> phrase.map { word -> word.groupingBy { it }.eachCount() } }
        .count { it.size == it.distinct().size }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D4(readRawInput("y2017/d4"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 455
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 186
}