package org.gristle.adventOfCode.y2015.d5

import org.gristle.adventOfCode.Day

class Y2015D5(input: String) : Day {
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

    override fun part1() = strings.count { it.isNicePart1() }

    override fun part2() = strings.count { it.isNicePart2() }
}

fun main() = Day.runDay(5, 2015, Y2015D5::class) // 255, 55
