package org.gristle.adventOfCode.y2015.d1

import org.gristle.adventOfCode.Day

class Y2015D1(input: String) : Day {
    private val floorChanges: Sequence<Int> = input.asSequence().map { if (it == '(') 1 else -1 }
    override fun part1(): Int = floorChanges.sum()
    override fun part2(): Int = floorChanges.runningFold(0, Int::plus).indexOfFirst { it == -1 }
}

fun main() = Day.runDay(1, 2015, Y2015D1::class) // 280, 1797
