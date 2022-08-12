package org.gristle.adventOfCode.y2019.d1

import org.gristle.adventOfCode.utilities.*
import kotlin.math.max

object Y2019D1 {
    private val input = readInput("y2019/d1")
    val modules = input.map { it.toInt() }

    tailrec fun fuel(remaining: Int, total: Int = 0): Int {
        return if (remaining == 0) {
            total
        } else {
            val fuel = max(0, remaining / 3 - 2)
            fuel(fuel,total + fuel)
        }
    }

    fun part1() = modules.sumOf { it / 3 - 2 }

    fun part2() = modules.sumOf { fuel(it) }

}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D1.part1()} (${elapsedTime(time)}ms)") // 3325347
    time = System.nanoTime()
    println("Part 2: ${Y2019D1.part2()} (${elapsedTime(time)}ms)") // 4985145
}