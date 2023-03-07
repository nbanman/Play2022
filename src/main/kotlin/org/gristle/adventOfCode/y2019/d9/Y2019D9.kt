package org.gristle.adventOfCode.y2019.d9

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2019.IntCode.IntCode

class Y2019D9(input: String) : Day {

    private val initialState = input
        .split(',')
        .map { it.toLong() }

    override fun part1(): Long {
        val computer = IntCode("A", initialState, 1L)
        computer.run()
        return computer.output.peekLast()
    }

    override fun part2(): Long {
        val computer = IntCode("B", initialState, 2L)
        computer.run()
        return computer.output.peekLast()
    }
}

fun main() = Day.runDay(Y2019D9::class)

//    Class creation: 19ms
//    Part 1: 2870072642 (2ms)
//    Part 2: 58534 (84ms)
//    Total time: 105ms