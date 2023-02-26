package org.gristle.adventOfCode.y2016.d6

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.transpose

class Y2016D6(input: String) : Day {
    private val columns: List<Map<Char, Int>> = input.lines(String::toList).transpose().map { it.eachCount() }
    override fun part1() = columns.map { column -> column.maxBy { it.value }.key }.joinToString("")
    override fun part2() = columns.map { column -> column.minBy { it.value }.key }.joinToString("")
}

fun main() = Day.runDay(6, 2016, Y2016D6::class) // asvcbhvg, odqnikqv