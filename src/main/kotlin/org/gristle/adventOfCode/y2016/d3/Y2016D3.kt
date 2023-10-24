package org.gristle.adventOfCode.y2016.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.collate
import org.gristle.adventOfCode.utilities.getInts

class Y2016D3(private val input: String) : Day {

    private fun isValid(triangle: List<Int>) = triangle.sorted().let { (a, b, c) -> a + b > c }

    private inline fun solve(rearrange: Sequence<Int>.() -> Sequence<Int> = { this }) = input
        .getInts()
        .rearrange()
        .chunked(3)
        .count(::isValid)

    override fun part1() = solve()
    override fun part2() = solve { collate(3).flatten() }
}

fun main() = Day.runDay(Y2016D3::class)

//    Class creation: 17ms
//    Part 1: 1032 (10ms)
//    Part 2: 1838 (5ms)
//    Total time: 33ms