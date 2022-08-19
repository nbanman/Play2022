package org.gristle.adventOfCode.y2017.d17

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

object Y2017D17 {
    private val input = readRawInput("y2017/d17").toInt()

    fun part1(): Int {
        val buffer = mutableListOf(0)
        var currPos = 0
        for (n in 1..2017) {
            currPos = (currPos + input) % buffer.size + 1
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
            val fits = (n - currPos) / input
            n += fits + 1
            currPos = (currPos + (fits + 1) * (input + 1) - 1) % n + 1
        }
        return result
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D17.part1()} (${elapsedTime(time)}ms)") // 1547
    time = System.nanoTime()
    println("Part 2: ${Y2017D17.part2()} (${elapsedTime(time)}ms)") // 31154878
}