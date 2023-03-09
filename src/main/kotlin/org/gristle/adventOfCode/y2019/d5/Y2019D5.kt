package org.gristle.adventOfCode.y2019.d5

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2019.ic.FintCode

class Y2019D5(private val input: String) : Day {
    override fun part1() = FintCode(input).run(listOf(1)).last()
    override fun part2() = FintCode(input).run(listOf(5)).last()
}

fun main() = Day.runDay(Y2019D5::class)

//    Class creation: 19ms
//    Part 1: 7839346 (0ms)
//    Part 2: 447803 (0ms)
//    Total time: 20ms