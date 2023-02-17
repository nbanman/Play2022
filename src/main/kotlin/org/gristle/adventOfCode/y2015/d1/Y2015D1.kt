package org.gristle.adventOfCode.y2015.d1

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2015D1(input: String) {
    private val floorChanges: Sequence<Int> = input.asSequence().map { if (it == '(') 1 else -1 }
    fun part1(): Int = floorChanges.sum()
    fun part2(): Int = floorChanges.runningFold(0, Int::plus).indexOfFirst { it == -1 }
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2015D1(getInput(1, 2015))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 280
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 1797
    println("Total time: ${timer.elapsed()}ms")
}
