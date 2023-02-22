package org.gristle.adventOfCode.y2016.d15

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts

private typealias Disc = Pair<Int, Int>

class Y2016D15(input: String) : Day {

    // Parse input into Discs
    private val discs = input
        .getInts()
        .chunked(4) { it[1] to -it[0] - it[3] }

    // Uses a sieve version of CRT
    private fun solve(discs: Sequence<Disc>): Int {
        val (_, answer) = discs.fold(1 to 0) { (positions, offset), (dPositions, dOffset) ->
            val nextOffset = generateSequence(offset) { it + positions }
                .first { (it - dOffset).mod(dPositions) == 0 }
            positions * dPositions to nextOffset
        }
        return answer
    }

    override fun part1() = solve(discs)

    override fun part2() = solve(discs + listOf(11 to -7))
}

fun main() = Day.runDay(15, 2016, Y2016D15::class) // 122318, 3208583