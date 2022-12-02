package org.gristle.adventOfCode.y2022.d2

import org.gristle.adventOfCode.utilities.*

class Y2022D2(input: String) {

    private val lines = input.lines().map { line ->
        line
    }
    private val nestedLines = input.blankSplit().map { chunk ->
        chunk.lines().map { line ->
            line
        }
    }
    private val pattern = Regex("""""")

    private val parsed = input
        .groupValues(pattern)

    fun part1() = parsed

    fun part2() = parsed
}

fun main() {
    val timer = Stopwatch(start = true)
    val input = getInput(2, 2022)
    val testInput = listOf("""""")
    val solver = Y2022D2(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}