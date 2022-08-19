package org.gristle.adventOfCode.y2016.d18

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

// Not refactored; pretty bad!
object Y2016D18 {
    private val input = readRawInput("y2016/d18")

    private fun safeTiles(numRows: Int): Int {
        var row1 = input
        var safeTiles = row1.count { it == '.' }
        for (i in 2..numRows) {
            row1 = ".$row1."
                .windowed(3)
                .map { if (it == "^^^" || it == "..." || it == "^.^" || it == ".^.") '.' else '^' }
                .joinToString("")
            safeTiles += row1.count { it == '.' }
        }
        return safeTiles
    }

    fun part1() = safeTiles(40)

    fun part2() = safeTiles(400_000)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D18.part1()} (${elapsedTime(time)}ms)") // 1987
    time = System.nanoTime()
    println("Part 2: ${Y2016D18.part2()} (${elapsedTime(time)}ms)") // 19984714
}