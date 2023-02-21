package org.gristle.adventOfCode.y2016.d15

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts

class Y2016D15(input: String) : Day {

    private class Disc(val number: Int, val positions: Int, val startPosition: Int) {
        fun passes(startingSecond: Int) = (startingSecond + number + startPosition) % positions == 0
    }

    // Parse input into Discs
    private val discs = input.getInts().chunked(4) { Disc(it[0], it[1], it[3]) }

    // Find the first number for which all discs pass
    private fun solve(discs: Sequence<Disc>) = generateSequence(0) { it + 1 }
        .first { index -> discs.all { it.passes(index) } }

    override fun part1() = solve(discs)

    override fun part2() = solve(discs + Disc(7, 11, 0))
}

fun main() = Day.runDay(15, 2016, Y2016D15::class) // 122318, 3208583