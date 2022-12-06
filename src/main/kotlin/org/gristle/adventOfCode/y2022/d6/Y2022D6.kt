package org.gristle.adventOfCode.y2022.d6

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D6(private val input: String) {
    fun solve(n: Int) = input.windowed(n).indexOfFirst { it.toSet().size == n } + n
    fun part1() = solve(4)
    fun part2() = solve(14)
}

fun main() {
    val timer = Stopwatch(start = true)
    val solver = Y2022D6(getInput(6, 2022))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}