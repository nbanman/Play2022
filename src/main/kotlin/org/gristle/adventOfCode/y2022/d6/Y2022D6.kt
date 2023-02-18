package org.gristle.adventOfCode.y2022.d6

import org.gristle.adventOfCode.Day

class Y2022D6(private val input: String) : Day {
    // Pretty one-liner (that I came up with) is 4x slower
    private fun slowSolve(n: Int) = n + input.windowed(n).indexOfFirst { it.toSet().size == n }

    // Alexander af Trolle's brilliant solution, modified to use an IntArray for kicks.
    private fun solve(n: Int): Int {
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

    override fun part1() = slowSolve(4)
    override fun part2() = slowSolve(14)
}

fun main() = Day.runDay(6, 2022, Y2022D6::class) // 1361, 3263