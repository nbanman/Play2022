package org.gristle.adventOfCode.y2023.d6

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getLongList
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.transpose

class Y2023D6(private val input: String) : Day {

    private fun waysToWin(race: Pair<Long, Long>): Int {
        val (time, distance) = race
        val halfTime = time / 2
        var backHalf = 0
        for (operand in halfTime..distance) {
            if (operand * (time - operand) > distance) backHalf++ else break
        }
        return (backHalf - 1) * 2 - if (time and 1L == 0L) 1 else 0
    }

    private inline fun solve(parse: (String) -> List<Long>): Int {
        val races: List<Pair<Long, Long>> = input
            .lines(parse)
            .transpose()
            .map { (time, distance) -> time to distance }

        return races.map(::waysToWin).reduce(Int::times)
    }

    override fun part1() = solve { it.getLongList() }

    override fun part2() = solve { it.filter(Char::isDigit).getLongList() }
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