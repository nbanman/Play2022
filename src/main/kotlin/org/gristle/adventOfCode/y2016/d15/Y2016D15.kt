package org.gristle.adventOfCode.y2016.d15

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

// Lightly refactored! (Ugly while loop to sequence).

class Y2016D15(input: String) {

    private val pattern = """Disc #(\d+) has (\d+) positions; at time=0, it is at position (\d+).""".toRegex()

    private data class Disc(val number: Int, val positions: Int, val startPosition: Int) {
        fun passes(startingSecond: Int) = (startingSecond + number + startPosition) % positions == 0
    }

    private val discs = input
        .groupValues(pattern)
        .map { Disc(it[0].toInt(), it[1].toInt(), it[2].toInt()) }

    fun part1() = generateSequence(0) { it + 1 }
        .first { index -> discs.all { it.passes(index) } }

    fun part2(): Int {
        val discs2 = discs + Disc(7, 11, 0)
        return generateSequence(0) { it + 1 }
            .first { index -> discs2.all { it.passes(index) } }
    }
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
