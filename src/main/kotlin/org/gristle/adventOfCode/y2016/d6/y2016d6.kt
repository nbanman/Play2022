package org.gristle.adventOfCode.y2016.d6

import org.gristle.adventOfCode.utilities.*

object Y2016D6 {
    private val lines = readInput("y2016/d6")
        .map { it.toList() }
        .transpose()

    fun part1() = lines
        .map { line -> line.eachCount().maxByOrNull { it.value }?.key ?: '?' }
        .joinToString("")

    fun part2() = lines
        .map { line -> line.eachCount().minByOrNull { it.value }?.key ?: '?' }
        .joinToString("")
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D6.part1()} (${elapsedTime(time)}ms)") // asvcbhvg
    time = System.nanoTime()
    println("Part 2: ${Y2016D6.part2()} (${elapsedTime(time)}ms)") // odqnikqv
}