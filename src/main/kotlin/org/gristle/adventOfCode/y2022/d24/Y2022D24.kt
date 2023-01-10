package org.gristle.adventOfCode.y2022.d24

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.toGrid

class Y2022D24(input: String) {

    private val valley = input.toGrid()

    fun part1() = "To be implemented"

    fun part2() = "To be implemented"
}

fun main() {
    val input = listOf(
        getInput(24, 2022),
        """#.#####
#.....#
#>....#
#.....#
#...v.#
#.....#
#####.#""",
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D24(input[1])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}