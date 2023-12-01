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

    private val tokenMap = buildMap<String, Int> {
        "one|two|three|four|five|six|seven|eight|nine"
            .split('|')
            .forEachIndexed { index, s ->
                val value = index + 1
                put(value.toString(), value)
                put(s, value)
                put(s.reversed(), value)
            }
    }

    override fun part2() = lines.sumOf { line ->
        val firstDigit = pattern.find(line)?.value?.let { tokenMap.getValue(it) }
            ?: throw IllegalArgumentException("No digit found in $line")
        val secondDigit = reversePattern.find(line.reversed())?.value?.let { tokenMap.getValue(it) }
            ?: throw IllegalArgumentException("No digit found in $line")
        firstDigit * 10 + secondDigit
    }
}

fun main() = Day.runDay(Y2023D1::class)

//    Class creation: 15ms
//    Part 1: 54388 (2ms)
//    Part 2: 53515 (9ms)
//    Total time: 27ms