package org.gristle.adventOfCode.y2023.d6

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getLongList
import org.gristle.adventOfCode.utilities.transpose
import kotlin.math.pow
import kotlin.math.sqrt

class Y2023D6(private val input: String) : Day {

    override fun part1() = solve { it.getLongList() }

    override fun part2() = solve { it.filter(Char::isDigit).getLongList() }

    private inline fun solve(parseLine: (String) -> List<Long>): Long {
        
        // parses input by parsing the digits in each input line according to the rules for each part, then
        // transposing the list of lists so that the vertical columns are grouped together.
        val races: List<List<Long>> = input
            .lines()
            .map(parseLine)
            .transpose()
        
        return races.map(::waysToWin).reduce(Long::times)
    }

    private fun waysToWin(race: List<Long>): Long {
        val (time, distance) = race
        
        // equation can be expressed as quadratic equation
        // D = distance, T = time, t = time pushing button
        // D = t(T - t) -> D = tT - t^2 -> -t^2 + tT - D = 0
        // which becomes: t = (-T ± sqrt(T^2 - 4 * D)) / -2
        val part = sqrt(time.toDouble().pow(2) - 4 * distance)
        val t1 = (-time + part) / -2
        val t2 = (-time - part) / -2
        return t2.toLong() - t1.toLong()
    }
}

fun main() = Day.runDay(Y2023D6::class)

//    Class creation: 2ms
//    Part 1: 2374848 (2ms)
//    Part 2: 39132886 (0ms)
//    Total time: 5ms

@Suppress("unused")
private val sampleInput = listOf(
    """Time:      7  15   30
Distance:  9  40  200
""" to "288",
)