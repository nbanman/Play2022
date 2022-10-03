package org.gristle.adventOfCode.y2017.d1

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toDigit

class Y2017D1(private val input: String) {

    fun part1(): Int {
        return (input.windowed(2) + listOf(input.first().toString() + input.last()))
            .filter { it[0] == it[1] }
            .sumOf { it[0].toDigit() }
    }

    fun part2() = input
        .mapIndexed { index, c ->
            if (c == input[(index + input.length / 2) % input.length]) {
                c.toDigit()
            } else 0
        }.sum()
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D1(readRawInput("y2017/d1"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1182
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1152
}