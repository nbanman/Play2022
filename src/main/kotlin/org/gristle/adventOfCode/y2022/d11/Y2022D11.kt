package org.gristle.adventOfCode.y2022.d11

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D11(input: String) {

    private val parsed = input


    fun part1() = "To be implemented"

    fun part2() = "To be implemented"
}

fun main() {
    val timer = Stopwatch(start = true)
    val solver = Y2022D11(getInput(11, 2022))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}