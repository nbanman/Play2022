package org.gristle.adventOfCode.y2015.d1

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D1(private val input: String) {

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
    val c = Y2015D1(readRawInput("y2015/d1"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 280
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1797
}
