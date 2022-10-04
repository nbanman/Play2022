package org.gristle.adventOfCode.y2019.d9

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2019.IntCode.IntCode

class Y2019D9(input: String) {

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
    val c = Y2019D9(readRawInput("y2019/d9"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 2870072642
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 58534
}