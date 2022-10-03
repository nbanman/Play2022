package org.gristle.adventOfCode.y2016.d19

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.log10
import kotlin.math.pow

// Not refactored; ugly but fast
class Y2016D19(input: String) {
    private val elves = input.toInt()

    private val exponent = (log10(elves.toDouble()) / log10(2.0)).toInt()

    private val exp = (log10(elves.toDouble()) / log10(3.0)).toInt()
    private val diff = elves - 3.0.pow(exp).toInt()

    fun part1() = (elves - (2.0.pow(exponent).toInt())) * 2 + 1

    fun part2() = if (diff == 0) {
        elves
    } else {
        val lastUp = 3.0.pow(exp).toInt()
        val ones = minOf(diff, lastUp)
        val twos = maxOf(diff - ones, 0)
        ones + twos * 2
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D19(readRawInput("y2016/d19"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1816277
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1410967
}