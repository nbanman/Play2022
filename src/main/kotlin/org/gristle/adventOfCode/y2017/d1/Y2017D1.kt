package org.gristle.adventOfCode.y2017.d1

import org.gristle.adventOfCode.Day

class Y2017D1(private val input: String) : Day {

    override fun part1(): Int {
        return (input.windowed(2) + listOf(input.first().toString() + input.last()))
            .filter { it[0] == it[1] }
            .sumOf { it[0].digitToInt() }
    }

    override fun part2() = input
        .mapIndexed { index, c ->
            if (c == input[(index + input.length / 2) % input.length]) {
                c.digitToInt()
            } else 0
        }.sum()
}

fun main() = Day.runDay(1, 2017, Y2017D1::class) // 1182, 1152