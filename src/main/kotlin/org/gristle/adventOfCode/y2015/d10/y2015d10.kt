package org.gristle.adventOfCode.y2015.d10

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

object Y2015D10 {
    private val input = readRawInput("y2015/d10")

    private fun lookAndSay(s: String) = buildString {
        var digit = s[0]
        var count = 1
        for (i in 1 until s.length) {
            if (s[i] == digit) {
                count++
            } else {
                append("$count$digit")
                digit = s[i]
                count = 1
            }
        }
        append("$count$digit")
    }

    private fun solve(n: Int) = generateSequence(input) { lookAndSay(it) }
        .take(n + 1)
        .last()
        .length

    fun part1() = solve(40)

    fun part2() = solve(50)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D10.part1()} (${elapsedTime(time)}ms)") // 492982
    time = System.nanoTime()
    println("Part 2: ${Y2015D10.part2()} (${elapsedTime(time)}ms)") // 6989950
}