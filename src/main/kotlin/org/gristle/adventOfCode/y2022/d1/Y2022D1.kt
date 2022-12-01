package org.gristle.adventOfCode.y2022.d1

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.readStrippedInput

class Y2022D1(input: String) {

    private val calories = input
        .split("\n\n")
        .map { it.getInts().sum() }
        .sortedDescending()

    fun part1() = calories.first()

    fun part2() = calories.take(3).sum()
}

fun main() {
    val timer = Stopwatch(true)
    val solver = Y2022D1(readStrippedInput("y2022/d1"))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 71300
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 209691
    println("Total time: ${timer.elapsed()}ms")
}