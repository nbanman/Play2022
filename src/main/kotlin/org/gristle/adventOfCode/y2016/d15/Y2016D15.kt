package org.gristle.adventOfCode.y2016.d15

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2016D15(input: String) {

    private class Disc(val number: Int, val positions: Int, val startPosition: Int) {
        fun passes(startingSecond: Int) = (startingSecond + number + startPosition) % positions == 0
    }

    // Regex to parse input
    private val pattern = """Disc #(\d+) has (\d+) positions; at time=0, it is at position (\d+).""".toRegex()

    // Parse input into Discs
    private val discs = input
        .groupValues(pattern, String::toInt)
        .map { Disc(it[0], it[1], it[2]) }

    // Find the first number for which all discs pass
    private fun solve(discs: List<Disc>) = generateSequence(0) { it + 1 }
        .first { index -> discs.all { it.passes(index) } }

    fun part1() = solve(discs)

    fun part2() = solve(discs + Disc(7, 11, 0))
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D15(readRawInput("y2016/d15"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 122318
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 3208583
}
