package org.gristle.adventOfCode.y2015.d5

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D5(input: String) {
    private val strings = input.lines()

    // Create Regex patterns for below functions
    private val vowels = Regex("[aeiou]")
    private val repeatedLetter = Regex("""([a-z])\1""")
    private val forbidden = Regex("ab|cd|pq|xy")
    private val repeatedDuo = Regex("""([a-z]{2}).*\1""")
    private val repeated1Between = Regex("""([a-z]).\1""")

    // Functions evaluate specific criteria
    private fun String.atLeast3Vowels() = vowels.findAll(this).count() >= 3
    private fun String.atLeastOneTwice() = repeatedLetter.containsMatchIn(this)
    private fun String.noForbiddenStrings() = !forbidden.containsMatchIn(this)
    private fun String.atLeastTwoTwice() = repeatedDuo.containsMatchIn(this)
    private fun String.repeatsWithOneBetween() = repeated1Between.containsMatchIn(this)

    private fun String.isNicePart1() = atLeast3Vowels() && atLeastOneTwice() && noForbiddenStrings()
    private fun String.isNicePart2() = atLeastTwoTwice() && repeatsWithOneBetween()

    fun part1() = strings.count { it.isNicePart1() }

    fun part2() = strings.count { it.isNicePart2() }
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2015D5(readRawInput("y2015/d5"))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${c.part1()} (${timer.lap()}ms)") // 255
    println("\tPart 2: ${c.part2()} (${timer.lap()}ms)") // 55
    println("Total time: ${timer.elapsed()}ms")
}