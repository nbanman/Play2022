package org.gristle.adventOfCode.y2023.d6

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getLongList
import org.gristle.adventOfCode.utilities.transpose

class Y2023D6(private val input: String) : Day {

    override fun part1() = solve { it.getLongList() }

    override fun part2() = solve { it.filter(Char::isDigit).getLongList() }

    private inline fun solve(parseLine: (String) -> List<Long>): Int {
        
        // parses input by parsing the digits in each input line according to the rules for each part, then
        // transposing the list of lists so that the vertical columns are grouped together.
        val races: List<List<Long>> = input
            .lines()
            .map(parseLine)
            .transpose()
        
        return races.map(::waysToWin).reduce(Int::times)
    }

    private fun waysToWin(race: List<Long>): Int {
        val (time, distance) = race
        var backHalfCount = 0
        
        // the largest product is when the two operands are the same (or as close as can be for odd times),
        // so start there. If product > distance, increment the count and the first operand. Do that until
        // every combination in the back half has been checked or product <= distance
        for (operand in (time / 2) until distance) {
            if (operand * (time - operand) > distance) backHalfCount++ else break
        }
        
        // only the back half was counted, so double it, since the first half is a mirror of the second.
        // even-numbered times lose one count because there is a singular "peak" that isn't mirrored on the other
        // side. e.g. t=8 -> 7, 12, 15, *16*, 15, 12, 7
        return (backHalfCount - 1) * 2 - if (time and 1L == 0L) 1 else 0
    }
}

fun main() = Day.runDay(Y2023D6::class)

//    Class creation: 2ms
//    Part 1: 2374848 (2ms)
//    Part 2: 39132886 (14ms)
//    Total time: 20ms

@Suppress("unused")
private val sampleInput = listOf(
    """Time:      7  15   30
Distance:  9  40  200
""" to "288",
)