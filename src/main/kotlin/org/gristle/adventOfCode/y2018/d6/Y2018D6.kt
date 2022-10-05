package org.gristle.adventOfCode.y2018.d6

import org.gristle.adventOfCode.utilities.*

class Y2018D6(input: String) {

    private val offsetCoordinates = input.lines().map { line ->
        val (x, y) = line.split(", ").map { it.toInt() }
        Coord(x, y)
    }

    private val ranges = offsetCoordinates.minMaxRanges()
    private val xRange = ranges.first
    private val yRange = ranges.second

    private val offset = Coord(xRange.first, yRange.first)
    private val coordinates = offsetCoordinates.map { it - offset }

    private val width = xRange.last - xRange.first + 1
    private val height = yRange.last - yRange.first + 1
    private val space = MutableGrid(width * height, width) { -2 }

    fun part1(): Int {
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
        val coord = space.coordOf(index)
        coordinates.sumOf { it.manhattanDistance(coord) } < 10_000
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D6(readRawInput("y2018/d6"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 5365
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 42513
}