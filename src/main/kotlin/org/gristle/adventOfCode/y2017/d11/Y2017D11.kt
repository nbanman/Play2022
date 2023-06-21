package org.gristle.adventOfCode.y2017.d11

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Hexagon

class Y2017D11(input: String) : Day {
    private val path: List<Hexagon> by lazy { input.split(',').runningFold(Hexagon(), Hexagon::hexAt) }
    override fun part1() = path.last().distance()
    override fun part2() = path.maxOf(Hexagon::distance)
}

fun main() = Day.runDay(Y2017D11::class)

//    Class creation: 2ms
//    Part 1: 747 (4ms)
//    Part 2: 1544 (2ms)
//    Total time: 9ms