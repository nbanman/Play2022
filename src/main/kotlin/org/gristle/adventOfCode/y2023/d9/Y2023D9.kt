package org.gristle.adventOfCode.y2023.d9

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList

class Y2023D9(input: String) : Day {

    private val patterns = input
        .lines()
        .map(String::getIntList)

    private fun findNext(pattern: List<Int>) = generateSequence(pattern) { it.zipWithNext { a, b -> b - a } }
        .takeWhile { next -> next.any { it != 0 } }
    
    override fun part1() = patterns.sumOf { pattern -> findNext(pattern).sumOf { it.last() } }

    override fun part2() = patterns.sumOf { pattern ->
        val initial = 0L.toInt() // lousy hack to make the compiler/linter shut up
        findNext(pattern)
            .map { it.first() }
            .toList()
            .foldRight(initial) { i, acc -> i - acc }
    }
}

fun main() = Day.runDay(Y2023D9::class)

//    Class creation: 20ms
//    Part 1: 1974913025 (6ms)
//    Part 2: 884 (4ms)
//    Total time: 32ms

@Suppress("unused")
private val sampleInput = listOf(
    """0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45""",
)