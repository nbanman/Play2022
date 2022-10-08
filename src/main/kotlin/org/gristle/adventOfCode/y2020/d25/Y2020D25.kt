package org.gristle.adventOfCode.y2020.d25

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D25(input: String) {

    private val divisor = 20201227

    private val keys = input.lines().map { it.toLong() }
    private val cardKey = keys[0]
    private val doorKey = keys[1]

    fun part1(): Long {
        val loopSize = generateSequence(0 to 1L) { (count, value) -> (count + 1) to (value * 7) % divisor }
            .first { (_, value) -> value == cardKey }
            .first
        return generateSequence(doorKey % divisor) { (it * doorKey) % divisor }
            .take(loopSize)
            .last()
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D25(readRawInput("y2020/d25"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 296776
}