package org.gristle.adventOfCode.y2022.d1

import org.gristle.adventOfCode.utilities.*

class Y2022D1(input: String) {

    private val calories = input
        .split("\n\n") // split on blank lines, so each elf's snacks are grouped
        // sum all the ints found in each elf's string, then put them all in a descending priority queue
        .toPriorityQueueDescending { it.getInts().sum() }
        .poll(3) // grab the top three contenders

    fun part1(): Int = calories.first() // peek at the top of the priority queue, which holds the highest sum

    fun part2() = calories.sum() // sum those three
}

fun main() {
    val timer = Stopwatch(start = true)
    val solver = Y2022D1(getInput(1, 2022))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 71300
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 209691
    println("Total time: ${timer.elapsed()}ms") // 63ms
}