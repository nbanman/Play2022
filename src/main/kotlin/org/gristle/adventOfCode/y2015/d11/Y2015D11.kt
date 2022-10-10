package org.gristle.adventOfCode.y2015.d11

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D11(input: String) {
    private fun isValid(s: String) = hasStraight(s) && noConfusion(s) && twoPairs(s)

    private fun increment(s: String): String {
        val changeIndex = s.indexOfLast { it != 'z' }
        val zs = s.lastIndex - changeIndex
        return s.substring(0, changeIndex) + (s[changeIndex] + 1) + (1..zs).fold("") { acc, _ -> acc + 'a' }
    }

    private fun hasStraight(s: String): Boolean {
        return s.windowed(3).any { trip ->
            ('a'..'z').contains(trip[0])
                    && trip[1] - trip[0] == 1
                    && trip[2] - trip[1] == 1
        }
    }

    private fun noConfusion(s: String): Boolean {
        return !"""[iol]"""
            .toRegex()
            .containsMatchIn(s)
    }

    private fun twoPairs(s: String): Boolean {
        return """([a-z])\1"""
            .toRegex()
            .findAll(s)
            .toList()
            .let {
                it.size > 1
            }
    }

    private val nextTwo = generateSequence(input, ::increment) // sequence starts with password and increments by one
        .drop(1) // do not consider the current password
        .filter(::isValid) // only consider valid passwords
        .take(2) // grab the first two
        .toList() // terminate sequence

    fun part1() = nextTwo.first()

    fun part2() = nextTwo.last()
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D11(readRawInput("y2015/d11"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // hxbxxyzz
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // hxcaabcc
}
