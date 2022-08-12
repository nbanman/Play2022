package org.gristle.adventOfCode.y2018.d6

import org.gristle.adventOfCode.utilities.*

object Y2018D6 {
    private val input = readInput("y2018/d6")

    val offsetCoordinates = input.map { line ->
        val (x, y) = line.split(", ").map { it.toInt() }
        Coord(x, y)
    }

    val ranges = offsetCoordinates.minMaxRanges()
    val xRange = ranges.first
    val yRange = ranges.second

    val offset = Coord(xRange.first, yRange.first)
    val coordinates = offsetCoordinates.map { it - offset }

    val space = List((xRange.last - xRange.first + 1) * (yRange.last - yRange.first + 1)) { -2 }
        .toMutableGrid(xRange.last - xRange.first + 1)

    fun part1(): Int {
        for (index in space.indices) {
            if (space[index] >= 0) continue
            val coord = space.coordIndex(index)
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
        val borderingIds = (space.row(0) + space.row(space.height - 1) +
                space.column(0) + space.column(space.width - 1)).toSet()

        return space
            .filter { it >= 0 && it !in borderingIds }
            .groupingBy { it }
            .eachCount()
            .entries
            .maxOf { it.value }
    }

    fun part2() = space.withIndex().count { (index, _) ->
        val coord = space.coordIndex(index)
        coordinates.sumOf { it.manhattanDistance(coord) } < 10_000
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2018D6.part1()} (${elapsedTime(time)}ms)") // 5365
    time = System.nanoTime()
    println("Part 2: ${Y2018D6.part2()} (${elapsedTime(time)}ms)") // 42513
}