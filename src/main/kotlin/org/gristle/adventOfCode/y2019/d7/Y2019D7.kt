package org.gristle.adventOfCode.y2019.d7

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getPermutations
import org.gristle.adventOfCode.y2019.IntCode.IntCode

class Y2019D7(input: String) : Day {
    val initialState = input
        .split(',')
        .map { it.toLong() }

    override fun part1(): Long = (0L..4L).getPermutations().maxOf { combo ->
        val c1 = IntCode("A", initialState, combo[0])
        val c2 = IntCode("B", initialState, combo[1], c1.output)
        val c3 = IntCode("C", initialState, combo[2], c2.output)
        val c4 = IntCode("D", initialState, combo[3], c3.output)
        val c5 = IntCode("E", initialState, combo[4], c4.output)
        val computers = listOf(c1, c2, c3, c4, c5)
        while (computers.any { !it.isDone }) {
            computers.forEach { it.run() }
        }
        c5.output.peekLast()
        }

    override fun part2(): Long = (5L..9L).getPermutations().maxOf { combo ->
        val c1 = IntCode("A", initialState, combo[0])
        val c2 = IntCode("B", initialState, combo[1], c1.output)
        val c3 = IntCode("C", initialState, combo[2], c2.output)
        val c4 = IntCode("D", initialState, combo[3], c3.output)
        val c5 = IntCode("E", initialState, combo[4], c4.output)
        c1.input = c5.output

        val computers = listOf(c1, c2, c3, c4, c5)
        while (computers.any { !it.isDone }) {
            computers.forEach { it.run() }
        }
        c5.output.peekLast()
    }
}

fun main() = Day.runDay(Y2019D7::class)

//    Class creation: 17ms
//    Part 1: 24405 (50ms)
//    Part 2: 8271623 (25ms)
//    Total time: 94ms
