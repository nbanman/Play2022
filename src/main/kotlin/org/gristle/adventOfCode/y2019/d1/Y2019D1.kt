package org.gristle.adventOfCode.y2019.d1

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.max

class Y2019D1(input: String) {
    private val modules = input.lines().map { it.toInt() }

    private tailrec fun fuel(remaining: Int, total: Int = 0): Int {
        return if (remaining == 0) {
            total
        } else {
            val fuel = max(0, remaining / 3 - 2)
            fuel(fuel, total + fuel)
        }
    }

    fun part1() = modules.sumOf { it / 3 - 2 }

    fun part2() = modules.sumOf { fuel(it) }

}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D1(readRawInput("y2019/d1"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 3325347
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 4985145
}