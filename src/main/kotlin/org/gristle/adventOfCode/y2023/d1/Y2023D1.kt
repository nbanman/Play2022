package org.gristle.adventOfCode.y2023.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.toDigit

class Y2023D1(input: String) : Day {

    private val lines = input.lines()

    override fun part1() = lines.sumOf { line ->
        line.first { it.isDigit() }.toDigit() * 10 + line.last { it.isDigit() }.toDigit()
    }

    override fun part2(): Int {
        val pattern = Regex("""[1-9]|one|two|three|four|five|six|seven|eight|nine""")
        val reversePattern = Regex("""[1-9]|eerht|enin|eno|evif|owt|ruof|xis|neves|thgie""")

        val tokenMap: Map<String, Int> = buildMap {
            "one|two|three|four|five|six|seven|eight|nine"
                .split('|')
                .forEachIndexed { index, s ->
                    val value = index + 1
                    put(value.toString(), value)
                    put(s, value)
                    put(s.reversed(), value)
                }
        }

        return lines.sumOf { line ->
            val firstDigit: Int = getDigit(line, pattern, tokenMap)
            val secondDigit: Int = getDigit(line.reversed(), reversePattern, tokenMap)
            firstDigit * 10 + secondDigit
        }
    }

    private fun getDigit(line: String, pattern: Regex, tokenMap: Map<String, Int>): Int = pattern
        .find(line)
        ?.value
        ?.let { tokenMap.getValue(it) }
        ?: throw IllegalArgumentException("No digit found in $line")
}

fun main() = Day.runDay(Y2023D1::class)

//    Class creation: 15ms
//    Part 1: 54388 (2ms)
//    Part 2: 53515 (9ms)
//    Total time: 27ms