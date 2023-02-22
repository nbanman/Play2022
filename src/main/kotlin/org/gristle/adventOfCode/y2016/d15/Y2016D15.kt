package org.gristle.adventOfCode.y2016.d15

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getLongs

private typealias Disc = Pair<Long, Long>

class Y2016D15(input: String) : Day {

    // Parse input into Discs
    private val discs = input
        .getLongs()
        .chunked(4) { it[1] to -it[0] - it[3] }

    // Uses a sieve version of CRT
    private fun solve(discs: Sequence<Disc>): Long {
        val (_, answer) = discs.fold(1L to 0L) { (interval, seconds), (positions, offset) ->

            // take the current number of seconds that works for the previous discs, and keep adding the current
            // interval until it works for the next disc.
            val nextSeconds = generateSequence(seconds) { it + interval }
                .first { (it - offset).mod(positions) == 0L }

            // values are coprime; next interval is current interval multiplied by the number of positions in next disc
            val nextInterval = interval * positions

            nextInterval to nextSeconds
        }
        return answer
    }

    override fun part1() = solve(discs)

    override fun part2() = solve(discs + listOf(11L to -7L))
}

fun main() = Day.runDay(15, 2016, Y2016D15::class) // 122318, 3208583 (11ms creation, 5ms pt 1, 1ms pt 2)