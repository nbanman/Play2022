package org.gristle.adventOfCode.y2015.d5

import org.gristle.adventOfCode.utilities.*

object Y2015D5 {
    private val strings = readInput("y2015/d5")

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
    println("Part 1: ${Y2015D5.part1()} (${elapsedTime(time)}ms)") // 255
    time = System.nanoTime()
    println("Part 2: ${Y2015D5.part2()} (${elapsedTime(time)}ms)") // 55
}