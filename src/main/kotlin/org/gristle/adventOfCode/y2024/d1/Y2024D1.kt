package org.gristle.adventOfCode.y2024.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.collate
import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.getIntList
import org.gristle.adventOfCode.utilities.getInts
import kotlin.math.absoluteValue

class Y2024D1(val input: String) : Day {

    override fun part1() = input
        .getInts()
        .collate(2)
        .map { it.sorted() }
        .toList()
        .let { (a, b) -> a.zip(b) }
        .sumOf { (a, b) -> (b - a).absoluteValue }

    override fun part2(): Int {
        val (a, b) = test.getIntList().collate(2)
        val freq = b.eachCount()
        return a.sumOf { it * (freq[it] ?: 0) }
    }
}

fun main() = Day.runDay(Y2024D1::class)

private val test = """3   4
4   3
2   5
1   3
3   9
3   3
"""