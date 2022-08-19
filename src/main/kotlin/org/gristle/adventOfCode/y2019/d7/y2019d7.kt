package org.gristle.adventOfCode.y2019.d7

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.getPermutations
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2019.IntCode.IntCode

object Y2019D7 {
    private val code = readRawInput("y2019/d7")
    val initialState = code
        .split(',')
        .map { it.toLong() }

    fun part1(): Long = (0L..4L).getPermutations().maxOf { combo ->
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

    fun part2(): Long = (5L..9L).getPermutations().maxOf { combo ->
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

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D7.part1()} (${elapsedTime(time)}ms)") // 24405
    time = System.nanoTime()
    println("Part 2: ${Y2019D7.part2()} (${elapsedTime(time)}ms)") // 8271623
}