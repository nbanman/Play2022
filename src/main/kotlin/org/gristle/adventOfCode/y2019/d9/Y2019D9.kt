package org.gristle.adventOfCode.y2019.d9

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2019.ic.FintCode


class Y2019D9(private val input: String) : Day {

    override fun part1() = FintCode(input).run(listOf(1L))[0]

    override fun part2() = FintCode(input).run(listOf(2L))[0]
}

fun main() = Day.runDay(Y2019D9::class)

//    Class creation: 19ms
//    Part 1: 2870072642 (2ms)
//    Part 2: 58534 (84ms)
//    Total time: 105ms