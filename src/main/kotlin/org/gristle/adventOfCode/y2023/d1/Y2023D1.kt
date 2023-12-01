package org.gristle.adventOfCode.y2023.d1

import org.gristle.adventOfCode.Day

class Y2023D1(private val input: String) : Day {

    // grabs the first and last digits in each line, makes a 2-digit number from them, and sums them
    private fun calibrate(input: String): Int = input.lines().sumOf { line ->
        val first = line.first(Char::isDigit).digitToInt()
        val second = line.last(Char::isDigit).digitToInt()
        first * 10 + second
    }

    override fun part1() = calibrate(input)

    // supports spelled-out numbers by using String.replace to insert digits into the input.
    override fun part2(): Int {

        // replaces spelled-out numbers with digits. spellings can overlap so to prevent the replace from obliterating
        // a future replace, select prefixes and suffixes are included
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

        // runs replace on the input string for each replacement in the list
        val digitsAdded = replacements.fold(input) { acc, (original, replacement) ->
            acc.replace(original, replacement)
        }
        return calibrate(digitsAdded)
    }
}

fun main() = Day.runDay(Y2023D1::class)

//    Class creation: 2ms
//    Part 1: 54388 (14ms)
//    Part 2: 53515 (7ms)
//    Total time: 25ms
