package org.gristle.adventOfCode.y2017.d17

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D17(input: String) {
    val inputNum = input.toInt()

    fun part1(): Int {
        val buffer = mutableListOf(0)
        var currPos = 0
        for (n in 1..2017) {
            currPos = (currPos + inputNum) % buffer.size + 1
            buffer.add(currPos, n)
        }
        return buffer[currPos + 1]
    }

    fun part2(): Int {
        var currPos = 0
        var result = 0
        var n = 0
        val limit = 50_000_000
        while (n < limit) {
            if (currPos == 1) result = n
            val fits = (n - currPos) / inputNum
            n += fits + 1
            currPos = (currPos + (fits + 1) * (inputNum + 1) - 1) % n + 1
        }
        return result
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D17(readRawInput("y2017/d17"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1547
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 31154878
}