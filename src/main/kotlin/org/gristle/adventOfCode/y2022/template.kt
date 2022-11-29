package org.gristle.adventOfCode.y2022

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.readStrippedInput

class Template(input: String) {


    fun part1() = "To be implemented"

    fun part2() = "To be implemented"
}

fun main() {
    val timer = Stopwatch(start = true)
    val solver = Template(readStrippedInput("y2022/d"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("Part 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}