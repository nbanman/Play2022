package org.gristle.adventOfCode.y2015.d1

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D1(private val input: String) {

    private fun moveFloor(floor: Int, paren: Char) = floor + if (paren == '(') 1 else -1
    fun part1() = input.fold(0, this::moveFloor)

    tailrec fun part2(floor: Int = 0, index: Int = 0): Int =
        if (floor == -1) index else part2(moveFloor(floor, input[index]), index + 1)
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
