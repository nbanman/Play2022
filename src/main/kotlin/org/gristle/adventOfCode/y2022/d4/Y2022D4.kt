package org.gristle.adventOfCode.y2022.d4

import org.gristle.adventOfCode.utilities.*

class Y2022D4(input: String) {

    private val ranges = input
        .groupValues("""(\d+)-(\d+),(\d+)-(\d+)""", String::toInt)
        .map { it[0]..it[1] to it[2]..it[3] }

    fun part1() = ranges.count { (left, right) ->
        left.containsAll(right) || right.containsAll(left)
    }

    fun part2() = ranges.count { (left, right) ->
        left.overlaps(right)
    }
}

fun main() {
    val timer = Stopwatch(start = true)
    val solver = Y2022D4(getInput(4, 2022))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 605
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 914
    println("Total time: ${timer.elapsed()}ms")
}