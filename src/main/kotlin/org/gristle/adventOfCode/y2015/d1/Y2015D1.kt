package org.gristle.adventOfCode.y2015.d1

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D1(input: String) {
    private val floorChanges = input.map {
        when (it) {
            '(' -> 1
            ')' -> -1
            else -> throw IllegalArgumentException("Invalid input: non-parenthesis encountered")
        }
    }

    fun part1() = floorChanges.sum()

    fun part2(): Int {
        val floor = sequence {
            // sum the changes, yielding the running total at each step
            floorChanges.fold(0) { acc, i -> (acc + i).also { yield(it) } }
        }
        return floor.indexOfFirst { it == -1 } + 1
    }
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2015D1(readRawInput("y2015/d1"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 280
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 1797
}
