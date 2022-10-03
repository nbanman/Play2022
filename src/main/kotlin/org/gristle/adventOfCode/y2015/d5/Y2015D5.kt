package org.gristle.adventOfCode.y2015.d5

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D5(input: String) {
    private val strings = input.lines()

    private fun String.atLeast3Vowels() = "[aeiou]".toRegex().findAll(this).count() >= 3
    private fun String.atLeastOneTwice() = """([a-z])\1""".toRegex().containsMatchIn(this)
    private fun String.noForbiddenStrings() = !"ab|cd|pq|xy".toRegex().containsMatchIn(this)
    private fun String.isNicePart1() = atLeast3Vowels() && atLeastOneTwice() && noForbiddenStrings()

    private fun String.atLeastTwoTwice() = """([a-z]{2}).*\1""".toRegex().containsMatchIn(this)
    private fun String.repeatsWithOneBetween() = """([a-z]).\1""".toRegex().containsMatchIn(this)
    private fun String.isNicePart2() = atLeastTwoTwice() && repeatsWithOneBetween()

    fun part1() = strings.count { it.isNicePart1() }

    fun part2() = strings.count { it.isNicePart2() }

}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D5(readRawInput("y2015/d5"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 255
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 55
}