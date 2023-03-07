package org.gristle.adventOfCode.y2017.d11

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Hexagon

class Y2017D11(input: String) : Day {
    private val dirs = input.split(',')

    val solution: Pair<Int, Int> = let {
        val home = Hexagon()
        dirs
            .fold(home to 0) { (hex, furthest), step ->
                val intermediate = hex.hexAt(step)
                intermediate to maxOf(furthest, intermediate.distance(home))
            }.let { (hex, furthest) -> hex.distance(home) to furthest }
    }

    override fun part1() = solution.first

    override fun part2() = solution.second
}

fun main() = Day.runDay(Y2017D11::class)

//    Class creation: 19ms
//    Part 1: 747 (0ms)
//    Part 2: 1544 (0ms)
//    Total time: 19ms