package org.gristle.adventOfCode.y2022.d6

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D6(private val input: String) {
    // Pretty one-liner is 4x slower
    // fun solve(n: Int) = n + input.windowed(4).indexOfFirst { it.toSet().size == n }
    
    fun solve(n: Int): Int {
        var distinct = 0
        val counts = IntArray(26)

        return 1 + input.indices.first { index ->
            if (index >= n) {
                when (--counts[input[index - n] - 'a']) {
                    0 -> distinct--
                    1 -> distinct++
                }
            }
            when (++counts[input[index] - 'a']) {
                1 -> distinct++
                2 -> distinct--
            }
            distinct == n
        }
    }

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