package org.gristle.adventOfCode.y2017.d4

import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D4(input: String) {

    private val passphrases = input.lines().map { it.split(' ') }

    private fun <T> List<List<T>>.countUnique() = count { it == it.distinct() }

    fun part1() = passphrases.countUnique()

    fun part2() = passphrases
        .map { phrase -> phrase.map { word -> word.eachCount() } } // convert words into letter distributions
        .countUnique()
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