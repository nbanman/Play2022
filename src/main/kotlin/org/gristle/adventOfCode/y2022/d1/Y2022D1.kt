package org.gristle.adventOfCode.y2022.d1

import org.gristle.adventOfCode.utilities.*

class Y2022D1(input: String) {

    private val calories = input
        .split("\n\n")
        .toPriorityQueueDescending { it.getInts().sum() }

    fun part1(): Int = calories.peek()

    fun part2() = calories.poll(3).sum()
}

fun main() {
    val timer = Stopwatch(start = true)
    val solver = Y2022D1(getInput(1, 2022))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 71300
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 209691
    println("Total time: ${timer.elapsed()}ms") // 63ms
}