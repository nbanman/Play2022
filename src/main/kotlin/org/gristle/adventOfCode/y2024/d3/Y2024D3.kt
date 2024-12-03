package org.gristle.adventOfCode.y2024.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getLongs
import org.gristle.adventOfCode.utilities.gvs

class Y2024D3(val input: String) : Day {

    override fun part1(): Long = input
        .gvs(Regex("""mul\((\d+),(\d+)\)""")) { it.toLong() }
        .sumOf { (a, b) -> a * b }

    override fun part2(): Long = Regex("""mul\(\d+,\d+\)|do\(\)|don't\(\)""")
        .findAll(input)
        .map(MatchResult::value)
        .fold(0L to true) { (sum, active), instruction ->
            val newActive = when (instruction) {
                "do()" -> true
                "don't()" -> false
                else -> active
            }
            val newSum = sum + if (active && instruction.startsWith("mul")) {
                instruction.getLongs().reduce(Long::times)
            } else {
                0
            }
            newSum to newActive
        }.first
}

fun main() = Day.runDay(Y2024D3::class)

@Suppress("unused")
private val test = listOf(
    """xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))""",
    """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""
)

//    Class creation: 1ms
//    Part 1: 161 (5ms)
//    Part 2: 48 (4ms)
//    Total time: 12ms