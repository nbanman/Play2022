package org.gristle.adventOfCode.y2023.d6

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getLongList
import org.gristle.adventOfCode.utilities.quadraticFormula
import org.gristle.adventOfCode.utilities.transpose

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
        // D = t(T - t) => (-1)t^2 + (T)t + (-D) = 0
        val (root1, root2) = quadraticFormula(-1, time, -distance)
        return root2.toLong() - root1.toLong()
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