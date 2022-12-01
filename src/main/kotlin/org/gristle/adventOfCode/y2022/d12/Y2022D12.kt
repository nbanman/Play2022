package org.gristle.adventOfCode.y2022.d12

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.readStrippedInput

class Y2022D12(input: String) {


    fun part1() = "To be implemented"

    fun part2() = "To be implemented"
}

fun main() {
    getInput(12, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D12(readStrippedInput("y2022/d12"))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}