package org.gristle.adventOfCode.y2015.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.md5

class Y2015D4(private val input: String) : Day {
    private fun solve(digitLength: Int) = generateSequence(1) { it + 1 }
        .first { i -> (input + i).md5().take(digitLength).all { it == '0' } }

    override fun part1() = solve(5)
    override fun part2() = solve(6)
}

fun main() = Day.runDay(Y2015D4::class)

//    Class creation: 16ms
//    Part 1: 117946 (103ms)
//    Part 2: 3938038 (1343ms)
//    Total time: 1463ms