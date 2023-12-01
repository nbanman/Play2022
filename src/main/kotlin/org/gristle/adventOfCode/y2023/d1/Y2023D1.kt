package org.gristle.adventOfCode.y2023.d1

import org.gristle.adventOfCode.Day

class Y2023D1(private val input: String) : Day {
    private fun calibrate(input: String): Int = input.lines().sumOf { line ->
        val first = line.first(Char::isDigit).digitToInt()
        val second = line.last(Char::isDigit).digitToInt()
        first * 10 + second
    }

    override fun part1() = calibrate(input)

    override fun part2(): Int {
        val replacements = listOf(
            "one" to "o1e",
            "two" to "t2o",
            "three" to "t3e",
            "four" to "4",
            "five" to "5e",
            "six" to "6",
            "seven" to "7n",
            "eight" to "e8t",
            "nine" to "n9e",
        )
        val numbersAdded = replacements.fold(input) { acc, (original, replacement) ->
            acc.replace(original, replacement)
        }
        return calibrate(numbersAdded)
    }
}

fun main() = Day.runDay(Y2023D1::class)

//    Class creation: 15ms
//    Part 1: 54388 (2ms)
//    Part 2: 53515 (9ms)
//    Total time: 27ms