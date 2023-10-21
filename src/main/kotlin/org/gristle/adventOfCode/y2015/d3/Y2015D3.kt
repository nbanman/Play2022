package org.gristle.adventOfCode.y2015.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.isEven

class Y2015D3(private val input: String) : Day {

    override fun part1() = input
        .runningFold(Coord.ORIGIN) { santa, dir -> santa.move(dir) }
        .toSet()
        .size

    override fun part2() = input
        .runningFoldIndexed(Coord.ORIGIN to Coord.ORIGIN) { index, (santa, robot), dir ->
            if (index.isEven()) {
                santa.move(dir) to robot
            } else {
                santa to robot.move(dir)
            }
        }.flatMap { (santa, robot) -> listOf(santa, robot) }
        .toSet()
        .size
}

fun main() = Day.runDay(Y2015D3::class)

//    Class creation: 21ms
//    Part 1: 2081 (8ms)
//    Part 2: 2341 (6ms)
//    Total time: 36ms