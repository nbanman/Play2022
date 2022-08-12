package org.gristle.adventOfCode.y2015.d1

import org.gristle.adventOfCode.utilities.*

object Y2015d1 {
    private val input = readRawInput("y2015/d1")

    fun part1() = input.fold(0) { acc, c -> acc + if (c == '(') 1 else -1 }

    tailrec fun part2(floor: Int = 0, index: Int = 0): Int {
        return if (floor == -1) index else part2(
            if (input[index] == '(') floor + 1 else floor - 1,
            index + 1
        )
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015d1.part1()} (${elapsedTime(time)}ms)") // 280
    time = System.nanoTime()
    println("Part 2: ${Y2015d1.part2()} (${elapsedTime(time)}ms)") // 1797
}
