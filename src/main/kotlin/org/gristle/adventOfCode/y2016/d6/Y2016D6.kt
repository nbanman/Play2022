package org.gristle.adventOfCode.y2016.d6

import org.gristle.adventOfCode.utilities.*

class Y2016D6(input: String) {
    private val lines = input.lines()
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
    val c = Y2016D6(readRawInput("y2016/d6"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // asvcbhvg
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // odqnikqv
}