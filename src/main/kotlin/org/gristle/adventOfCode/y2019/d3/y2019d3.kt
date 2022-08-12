package org.gristle.adventOfCode.y2019.d3

import org.gristle.adventOfCode.utilities.*

object Y2019D3 {
    private val input = readInput("y2019/d3")

    fun List<String>.wireUp() = fold(mutableListOf<Coord>()) { acc, instruction ->
        val last = if (acc.isNotEmpty()) acc.last() else Coord.ORIGIN
        val dir = when (instruction[0]) {
            'R' -> Nsew.EAST
            'L' -> Nsew.WEST
            'U' -> Nsew.NORTH
            else -> Nsew.SOUTH
        }
        for (i in 1..instruction.drop(1).toInt()) acc.add(last.move(dir, i))
        acc
    }

    private val wiresInstructions = input.map { it.split(',') }.map { it.wireUp() }

    private val intersections = wiresInstructions[0].intersect(wiresInstructions[1].toSet())

    fun part1() = intersections.minOf { it.manhattanDistance() }

    fun part2(): Int {
        return intersections.minOf { coord ->
            wiresInstructions.sumOf { it.indexOf(coord) + 1 }
        }
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D3.part1()} (${elapsedTime(time)}ms)") // 266
    time = System.nanoTime()
    println("Part 2: ${Y2019D3.part2()} (${elapsedTime(time)}ms)") // 19242 
}