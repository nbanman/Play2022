package org.gristle.adventOfCode.y2016.d19

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.log10
import kotlin.math.pow

// Not refactored; ugly but fast
object Y2016D19 {
    private val input = readRawInput("y2016/d19").toInt()

    val exponent = (log10(input.toDouble()) / log10(2.0)).toInt()

    val exp = (log10(input.toDouble()) / log10(3.0)).toInt()
    val diff = input - 3.0.pow(exp).toInt()

    fun part1() = (input - (2.0.pow(exponent).toInt())) * 2 + 1

    fun part2() = if (diff == 0) {
        input
    } else {
        val nextUp = 3.0.pow(exp + 1).toInt()
        val lastUp = 3.0.pow(exp).toInt()
        val ones = minOf(diff, lastUp)
        val twos = maxOf(diff - ones, 0)
        ones + twos * 2
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D19.part1()} (${elapsedTime(time)}ms)") // 1816277
    time = System.nanoTime()
    println("Part 2: ${Y2016D19.part2()} (${elapsedTime(time)}ms)") // 1410967
}