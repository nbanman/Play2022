package org.gristle.adventOfCode.y2016.d6

import org.gristle.adventOfCode.utilities.*

class Y2016D6(input: String) {
    private val columns = input
        .lines(String::toList)
        .transpose()
        .map { it.eachCount() }
    
    fun part1() = columns
        .map { column -> column.maxBy { it.value }.key }
        .joinToString("")

    fun part2() = columns
        .map { column -> column.minBy { it.value }.key }
        .joinToString("")
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2016D6(readRawInput("y2016/d6"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // asvcbhvg
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // odqnikqv
    println("Total time: ${timer.elapsed()}ms")
}