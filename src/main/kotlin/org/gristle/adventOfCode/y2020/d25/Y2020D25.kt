package org.gristle.adventOfCode.y2020.d25

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D25(input: String) {

    private val keys = input.lines().map { it.toLong() }
    private val cardKey = keys[0]
    private val doorKey = keys[1]

    fun part1(): Long {
        var loopSize = 0L
        var value = 1L
        var subjectNumber = 7L
        while (value != cardKey) {
            loopSize++
            value = (value * subjectNumber) % 20201227L
        }
        subjectNumber = doorKey
        value = 1
        for (i in 1..loopSize) {
            value = (value * subjectNumber) % 20201227L
        }
        return value
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D25(readRawInput("y2020/d25"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 296776
}