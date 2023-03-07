package org.gristle.adventOfCode.y2019.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew

class Y2019D3(input: String) : Day {

    private fun List<String>.wireUp(): List<Coord> = fold(mutableListOf()) { acc, instruction ->
        val last = if (acc.isNotEmpty()) acc.last() else Coord.ORIGIN
        val dir = when (instruction[0]) {
            'R' -> Nsew.EAST
            'L' -> Nsew.WEST
            'U' -> Nsew.NORTH
            'D' -> Nsew.SOUTH
            else -> throw IllegalArgumentException()
        }
        for (i in 1..instruction.drop(1).toInt()) acc.add(last.move(dir, i))
        acc
    }

    private val wiresInstructions = input.lines().map { it.split(',') }.map { it.wireUp() }

    private val intersections = wiresInstructions[0].intersect(wiresInstructions[1].toSet())

    override fun part1() = intersections.minOf(Coord::manhattanDistance)

    override fun part2(): Int {
        return intersections.minOf { coord ->
            wiresInstructions.sumOf { it.indexOf(coord) + 1 }
        }
    }
}

fun main() = Day.runDay(Y2019D3::class)

//    Class creation: 106ms
//    Part 1: 266 (0ms)
//    Part 2: 19242 (31ms)
//    Total time: 137ms