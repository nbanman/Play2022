package org.gristle.adventOfCode.y2015.d11

import org.gristle.adventOfCode.utilities.elapsedTime

object Y2015D11 {
    private const val input = "hxbxwxba"

    private fun isValid(s: String) = hasStraight(s) && noConfusion(s) && twoPairs(s)

    private fun iterate(s: String): String {
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

    private val nextTwo = generateSequence(iterate(input)) { iterate(it) }
        .filter { isValid(it) }
        .take(2)
        .toList()

    fun part1() = nextTwo.first()

    fun part2() = nextTwo.last()
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D11.part1()} (${elapsedTime(time)}ms)") // hxbxxyzz
    time = System.nanoTime()
    println("Part 2: ${Y2015D11.part2()} (${elapsedTime(time)}ms)") // hxcaabcc
}