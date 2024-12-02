package org.gristle.adventOfCode.y2024.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList

class Y2024D2(input: String) : Day {
    private val levels: List<List<Int>> = input.lines().map { it.getIntList() }

    private fun List<Int>.isSafe(): Boolean {
        val rng = if (first() < last()) -3..-1 else 1..3
        return asSequence().zipWithNext().all { (a, b) -> a - b in rng }
    }

    override fun part1() = levels.count { it.isSafe() }
    override fun part2() = levels.count { level ->
        level.isSafe() || level.indices
            .map { i -> level.subList(0, i) + level.subList(i + 1, level.size) }
            .any { it.isSafe() }
    }
}

fun main() = Day.runDay(Y2024D2::class)

@Suppress("unused")
private const val test = """7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9"""

//    Class creation: 20ms
//    Part 1: 591 (5ms)
//    Part 2: 621 (9ms)
//    Total time: 34ms