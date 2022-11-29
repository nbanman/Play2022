package org.gristle.adventOfCode.y2015.d10

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D10(private val input: String) {

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
    val timer = Stopwatch(true)
    val c = Y2015D10(readRawInput("y2015/d10"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 492982
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 6989950
    println("Total time: ${timer.elapsed()}ms")
}
