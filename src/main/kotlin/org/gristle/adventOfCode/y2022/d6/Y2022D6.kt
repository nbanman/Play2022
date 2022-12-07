package org.gristle.adventOfCode.y2022.d6

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D6(private val input: String) {
    // Pretty one-liner (that I came up with) is 4x slower
    fun slowSolve(n: Int) = n + input.windowed(4).indexOfFirst { it.toSet().size == n }

    // Alexander af Trolle's brilliant solution, modified to use an IntArray for kicks.
    fun solve(n: Int): Int {
        // tracks the index of the last time the character was encountered
        val indexMap = IntArray(26)
        // tracks the index of the last time a first character of a duplicate was encountered
        var duplicateIndex = 0
        var index = 0
        return input.indexOfFirst { c ->
            // update the index in the map and grab the previous index for that character 
            val lastSeen = indexMap[c - 'a']
            indexMap[c - 'a'] = index
            duplicateIndex = duplicateIndex.coerceAtLeast(lastSeen) // update duplicateIndex
            // if the last duplicate's first character index is further than n characters away, predicate matched 
            index++ - duplicateIndex >= n
        } + 1
    }

    fun part1() = solve(4)
    fun part2() = solve(14)
}

fun main() {
    val input = getInput(6, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D6(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 1361
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 3263
    println("Total time: ${timer.elapsed()}ms") // 20ms
}