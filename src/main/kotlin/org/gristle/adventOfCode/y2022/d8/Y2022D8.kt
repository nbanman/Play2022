package org.gristle.adventOfCode.y2022.d8

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.readStrippedInput

class Y2022D8(input: String) {

    private val parsed = input


    fun part1() = "To be implemented"

    fun part2() = "To be implemented"
}

fun main() {
    getInput(8, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D8(readStrippedInput("y2022/d8"))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}