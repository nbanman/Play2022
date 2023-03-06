package org.gristle.adventOfCode.y2018.d6

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.MutableGrid
import org.gristle.adventOfCode.utilities.minMaxRanges

class Y2018D6(input: String) : Day {

    private val offsetCoordinates = input.lines().map { line ->
        val (x, y) = line.split(", ").map(String::toInt)
        Coord(x, y)
    }

    private val ranges = offsetCoordinates.minMaxRanges()
    private val xRange = ranges.first
    private val yRange = ranges.second

    private val offset = Coord(xRange.first, yRange.first)
    private val coordinates = offsetCoordinates.map { it - offset }

    private val width = xRange.last - xRange.first + 1
    private val height = yRange.last - yRange.first + 1
    private val space = MutableGrid(width, height) { -2 }

    init {
        for (index in space.indices) {
            if (space[index] >= 0) continue
            val coord = space.coordOf(index)
            val (closest, next) = coordinates
                .map { it.manhattanDistance(coord) }
                .withIndex()
                .sortedBy { it.value }
                .take(2)
            if (closest.value == next.value) {
                space[index] = -1
            } else {
                space[index] = closest.index
            }
        }
    }

    override fun part1(): Int {
        val borderingIds = (space.row(0) + space.row(space.height - 1) +
                space.column(0) + space.column(space.width - 1)).toSet()

        return space
            .filter { it >= 0 && it !in borderingIds }
            .groupingBy { it }
            .eachCount()
            .entries
            .maxOf { it.value }
    }

    override fun part2() = space.indices.count { index ->
        coordinates.sumOf { it.manhattanDistance(space.coordOf(index)) } < 10_000
    }
}

fun main() = Day.runDay(Y2018D6::class)

//    Class creation: 432ms
//    Part 1: 5365 (35ms)
//    Part 2: 42513 (47ms)
//    Total time: 515ms