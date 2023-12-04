package org.gristle.adventOfCode.y2023.d1

import org.gristle.adventOfCode.Day

class Y2023D1b(val input: String) : Day {

    fun calibrationValue(fileContent: String): Int {

        var result = 0
        var firstLast: Pair<Int, Int> = Pair(0, 0)

        for (ch in fileContent) {

            if (ch == '\n') {

                if (firstLast.second == 0) firstLast = firstLast.copy(second = firstLast.first)
                result += firstLast.first * 10 + firstLast.second
                firstLast = Pair(0, 0)
                continue
            }

            /// I could do this like P2 below, but I wanted to try using when
            when (ch) {

                '1' -> firstLast = if (firstLast.first == 0) firstLast.copy(first = 1) else firstLast.copy(second = 1)
                '2' -> firstLast = if (firstLast.first == 0) firstLast.copy(first = 2) else firstLast.copy(second = 2)
                '3' -> firstLast = if (firstLast.first == 0) firstLast.copy(first = 3) else firstLast.copy(second = 3)
                '4' -> firstLast = if (firstLast.first == 0) firstLast.copy(first = 4) else firstLast.copy(second = 4)
                '5' -> firstLast = if (firstLast.first == 0) firstLast.copy(first = 5) else firstLast.copy(second = 5)
                '6' -> firstLast = if (firstLast.first == 0) firstLast.copy(first = 6) else firstLast.copy(second = 6)
                '7' -> firstLast = if (firstLast.first == 0) firstLast.copy(first = 7) else firstLast.copy(second = 7)
                '8' -> firstLast = if (firstLast.first == 0) firstLast.copy(first = 8) else firstLast.copy(second = 8)
                '9' -> firstLast = if (firstLast.first == 0) firstLast.copy(first = 9) else firstLast.copy(second = 9)
            }
        }

        return result
    }

    fun calibrationValueP2(fileContent: String): Int {

        var result = 0
        var firstLast: Pair<Int, Int> = Pair(0, 0)
        var buffer = ""
        for (ch in fileContent) {


            if (ch == '\n') {

                if (firstLast.second == 0) firstLast = firstLast.copy(second = firstLast.first)
                result += firstLast.first * 10 + firstLast.second
                firstLast = Pair(0, 0)
                continue
            }

            buffer += ch;

            val numberWords = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
            for ((index, word) in numberWords.withIndex()) {
                if (word in buffer) {
                    firstLast =
                        if (firstLast.first == 0) firstLast.copy(first = index + 1) else firstLast.copy(second = index + 1)
                    buffer = buffer.last().toString(); /// in case of "sevenine" -> this is expected to be 79
                    break
                }
            }

            if (ch.isDigit()) {
                val digit = Character.getNumericValue(ch)
                firstLast = if (firstLast.first == 0) firstLast.copy(first = digit) else firstLast.copy(second = digit)
            }
        }
        return result
    }

    override fun part1() = calibrationValue(input)

    override fun part2() = calibrationValueP2(input)
}


fun main() = Day.runDay(Y2023D1b::class)