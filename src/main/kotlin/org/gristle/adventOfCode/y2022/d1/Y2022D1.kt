package org.gristle.adventOfCode.y2022.d1

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.readStrippedInput

class Y2022D1(input: String) {

    fun part1() = "To be implemented"

    fun part2() = "To be implemented"
}

fun main() {
    val timer = Stopwatch(true)
    val solver = Y2022D1(readStrippedInput("y2022/d1"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("Part 2: ${solver.part2()} (${timer.lap()}ms)") // 
}