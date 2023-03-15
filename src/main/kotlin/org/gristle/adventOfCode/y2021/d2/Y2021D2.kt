package org.gristle.adventOfCode.y2021.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord

class Y2021D2(input: String) : Day {

    val commands = input
        .lineSequence()
        .map { line -> line[0] to line.takeLastWhile { it.isDigit() }.toInt() }

    override fun part1(): Int = commands
        .fold(Coord.ORIGIN) { pos, (dir, amt) ->
            when (dir) {
                'f' -> pos.copy(x = pos.x + amt)
                'u' -> pos.copy(y = pos.y - amt)
                'd' -> pos.copy(y = pos.y + amt)
                else -> throw IllegalArgumentException("invalid command")
            }
        }.let { it.x * it.y }

    override fun part2(): Int = commands
        .fold(Coord.ORIGIN to 0) { (pos, aim), (dir, amt) ->
            when (dir) {
                'f' -> Coord(pos.x + amt, pos.y + aim * amt) to aim
                'u' -> pos to aim - amt
                'd' -> pos to aim + amt
                else -> throw IllegalArgumentException("invalid command")
            }
        }.let { (pos, _) -> pos.x * pos.y }
}

fun main() = Day.runDay(Y2021D2::class)

//    Class creation: 14ms
//    Part 1: 2117664 (9ms)
//    Part 2: 2073416724 (4ms)
//    Total time: 28ms