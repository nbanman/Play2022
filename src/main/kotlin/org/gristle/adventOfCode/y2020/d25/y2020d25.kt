package org.gristle.adventOfCode.y2020.d25

import org.gristle.adventOfCode.utilities.*

object Y2020D25 {
    private val input = readInput("y2020/d25")

    private val keys = input.map { it.toLong() }
    val cardKey = keys[0]
    val doorKey = keys[1]

    fun part1(): Long {
        var loopSize = 0L
        var value = 1L
        var subjectNumber = 7L
        while (value != cardKey) {
            loopSize++
            value = (value * subjectNumber) % 20201227L
        }
        subjectNumber = doorKey.toLong()
        value = 1
        for (i in 1..loopSize) {
            value = (value * subjectNumber) % 20201227L
        }
        return value
    }

}

fun main() {
    val time = System.nanoTime()
    println("Part 1: ${Y2020D25.part1()} (${elapsedTime(time)}ms)") // 296776 
}