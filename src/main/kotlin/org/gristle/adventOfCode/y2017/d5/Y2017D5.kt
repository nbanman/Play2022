package org.gristle.adventOfCode.y2017.d5

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D5(input: String) {

    val instructions = input.lines().map { it.toInt() }

    inline fun solve(incrementBehavior: (Int) -> Int): Int {
        val jumps = instructions.toMutableList()
        var pos = 0
        var counter = 0
        while (pos in jumps.indices) {
            counter++
            val inc = incrementBehavior(jumps[pos])
            jumps[pos] += inc
            pos += jumps[pos] - inc
        }
        return counter
    }

    fun part1() = solve { 1 }

    fun part2() = solve { jumps -> if (jumps >= 3) -1 else 1 }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D5(readRawInput("y2017/d5"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 373160
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 26395586
}