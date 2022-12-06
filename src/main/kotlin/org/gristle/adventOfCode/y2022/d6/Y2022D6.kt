package org.gristle.adventOfCode.y2022.d6

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D6(private val input: String) {
    // Pretty one-liner is 4x slower
    // fun solve(n: Int) = n + input.windowed(4).indexOfFirst { it.toSet().size == n }
    
    fun solve(n: Int): Int {
        // counts number of distinct values
        var distinct = 0

        // tracks the number of times a specific value exists in the n-sized window
        val counts = IntArray(26)

        // returns 1 plus the first index where the resulting window has all distinct values
        return 1 + input.indices.first { index ->
            // Only runs once the window reaches 'n' size. Removes the left-most value to make room for the next one.
            if (index >= n) {
                // Subtracts the left-most value from 'counts' and adjusts the 'distinct' count downward as necessary.
                when (--counts[input[index - n] - 'a']) {
                    0 -> distinct--
                    1 -> distinct++
                }
            }
            // Adds the next value from 'counts' and adjusts the 'distinct' count upward as necessary.
            when (++counts[input[index] - 'a']) {
                1 -> distinct++
                2 -> distinct--
            }
            distinct == n // Predicate
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