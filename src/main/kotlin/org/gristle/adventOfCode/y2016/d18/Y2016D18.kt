package org.gristle.adventOfCode.y2016.d18

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

typealias Row = String
class Y2016D18(private val input: String) {

    // Sequence counts number of '.' in each row. "row" is mutable state, confined to the sequence. After the
    // first call, each subsequent call mutates the internal state to find the next row.
    private val safeTiles = sequence {
        fun Row.nextRow() = ".$this."
            .windowed(3)
            .map { if (it == "^^^" || it == "..." || it == "^.^" || it == ".^.") '.' else '^' }
            .joinToString("")
        // Mutable state starts with input. Successive calls will find next row.
        var row = input

        do {
            yield(row.count { it == '.' })
            row = row.nextRow()
        } while (true)
    }

    private fun solve(rows: Int) = safeTiles.take(rows).sum()

    fun part1() = solve(40)

    fun part2() = solve(400_000)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D18(readRawInput("y2016/d18"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1987
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 19984714
}