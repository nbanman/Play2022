package org.gristle.adventOfCode.y2023.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.toDigit

class Y2023D1(input: String) : Day {

    private val lines = input.lines()

    override fun part1() = lines.sumOf { line ->
        line.first { it.isDigit() }.toDigit() * 10 + line.last { it.isDigit() }.toDigit()
    }

    private val pattern = """one|two|three|four|five|six|seven|eight|nine|[0-9]""".toRegex()
    private val reversePattern = """eno|owt|eerht|ruof|evif|xis|neves|thgie|enin|[0-9]""".toRegex()

    private fun String.toDigit() = when (this) {
        "one", "eno" -> 1
        "two", "owt" -> 2
        "three", "eerht" -> 3
        "four", "ruof" -> 4
        "five", "evif" -> 5
        "six", "xis" -> 6
        "seven", "neves" -> 7
        "eight", "thgie" -> 8
        "nine", "enin" -> 9
        else -> this.toInt()
    }

    override fun part2() = lines.sumOf { line ->
        val firstDigit = pattern.find(line)?.value?.toDigit()
            ?: throw IllegalArgumentException("No digit found in $line")
        val secondDigit = reversePattern.find(line.reversed())?.value?.toDigit()
            ?: throw IllegalArgumentException("No digit found in $line")
        firstDigit * 10 + secondDigit
    }
}

fun main() = Day.runDay(Y2023D1::class)