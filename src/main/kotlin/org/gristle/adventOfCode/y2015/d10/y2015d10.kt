package org.gristle.adventOfCode.y2015.d10

import org.gristle.adventOfCode.utilities.*

object Y2015D10 {
    private const val input = "1321131112"

    private fun String.lookAndSay(): String {
        val sb = StringBuilder()
        var digit = this[0]
        var count = 1
        for (i in 1 until length) {
            if (this[i] == digit) {
                count++
            } else {
                sb.append("$count$digit")
                digit = this[i]
                count = 1
            }
        }
        return sb.append("$count$digit").toString()
    }

    private fun lookAndSay(n: Int) =
        generateSequence(input) { it.lookAndSay() }.take(n + 1).last().length

    fun part1() = lookAndSay(40)

    fun part2() = lookAndSay(50)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D10.part1()} (${elapsedTime(time)}ms)") // 492982
    time = System.nanoTime()
    println("Part 2: ${Y2015D10.part2()} (${elapsedTime(time)}ms)") // 6989950
}