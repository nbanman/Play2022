package org.gristle.adventOfCode.y2016.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.getInts

class Y2016D1(input: String) : Day {

    private val moves: Sequence<Coord>

    init {
        var dir = Nsew.NORTH

        moves = input
            .splitToSequence(", ")
            .flatMap { instruction ->
                dir = if (instruction[0] == 'L') dir.left() else dir.right()
                List(instruction.getInts().first()) { dir }
            }.runningFold(Coord.ORIGIN, Coord::move)
    }

    override fun part1() = moves
        .last()
        .manhattanDistance()

    override fun part2(): Int {
        val visited = mutableSetOf<Coord>()
        return moves
            .first { !visited.add(it) }
            .manhattanDistance()
    }
}

fun main() = Day.runDay(Y2016D1::class)

//    Class creation: 6ms
//    Part 1: 226 (3ms)
//    Part 2: 79 (4ms)
//    Total time: 14ms