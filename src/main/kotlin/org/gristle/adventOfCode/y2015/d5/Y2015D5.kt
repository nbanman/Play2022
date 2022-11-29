package org.gristle.adventOfCode.y2015.d5

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D5(input: String) {
    private val strings = input.lines()

    private val regexVowels = Regex("[aeiou]")
    private fun String.atLeast3Vowels() = regexVowels.findAll(this).count() >= 3

    private val regexRepeat = Regex("""([a-z])\1""")
    private fun String.atLeastOneTwice() = regexRepeat.containsMatchIn(this)

    private val regexForbidden = Regex("ab|cd|pq|xy")
    private fun String.noForbiddenStrings() = !regexForbidden.containsMatchIn(this)
    private fun String.isNicePart1() = atLeast3Vowels() && atLeastOneTwice() && noForbiddenStrings()

    private val regex2Twice = Regex("""([a-z]{2}).*\1""")
    private fun String.atLeastTwoTwice() = regex2Twice.containsMatchIn(this)

    private val regexRepeatWith1Between = Regex("""([a-z]).\1""")
    private fun String.repeatsWithOneBetween() = regexRepeatWith1Between.containsMatchIn(this)
    private fun String.isNicePart2() = atLeastTwoTwice() && repeatsWithOneBetween()

    fun part1() = strings.count { it.isNicePart1() }

    fun part2() = strings.count { it.isNicePart2() }

}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2015D5(readRawInput("y2015/d5"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 255
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 55
    println(timer.stop())
}