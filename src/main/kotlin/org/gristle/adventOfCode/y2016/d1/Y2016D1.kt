package org.gristle.adventOfCode.y2016.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew

class Y2016D1(input: String) : Day {

    private val moves: Sequence<Coord>

    init {
        var direction = Nsew.NORTH
        moves = input
            .splitToSequence(", ")
            .map { command ->
                direction = when (command[0]) {
                    'L' -> direction.left()
                    'R' -> direction.right()
                    else -> throw IllegalArgumentException("Direction not recognized: ${command[0]}")
                }
                direction to command.drop(1).toInt()
            }.runningFold(Coord.ORIGIN) { pos, (dir, dist) -> pos.move(dir, dist) }
    }

    override fun part1() = moves.last().manhattanDistance()

    override fun part2(): Int {
        val visited = mutableSetOf(Coord.ORIGIN)
        moves
            .zipWithNext()
            .forEach { (prev, next) ->
                prev
                    .lineTo(next)
                    .drop(1)
                    .forEach { pos -> if (!visited.add(pos)) return pos.manhattanDistance() }
            }
        throw IllegalStateException("Movements completed with no location visited more than once!")
    }
}

fun main() = Day.runDay(Y2016D1::class)

//    Class creation: 6ms
//    Part 1: 226 (3ms)
//    Part 2: 79 (4ms)
//    Total time: 14ms