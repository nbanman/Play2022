package org.gristle.adventOfCode.y2019.d9

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.y2019.IntCode.IntCode

object Y2019D9 {
    private val input = readRawInput("y2019/d9")

    private val initialState = input
        .split(',')
        .map { it.toLong() }

    fun part1(): Long {
        val computer = IntCode("A", initialState, 1L)
        computer.run()
        return computer.output.peekLast()
    } 

    fun part2(): Long {
        val computer = IntCode("B", initialState, 2L)
        computer.run()
        return computer.output.peekLast()
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D9.part1()} (${elapsedTime(time)}ms)") // 2870072642
    time = System.nanoTime()
    println("Part 2: ${Y2019D9.part2()} (${elapsedTime(time)}ms)") // 58534 
}