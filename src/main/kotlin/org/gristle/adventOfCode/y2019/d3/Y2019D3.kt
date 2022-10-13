package org.gristle.adventOfCode.y2019.d3

import org.gristle.adventOfCode.utilities.*

class Y2019D3(input: String) {

    private fun List<String>.wireUp(): List<Coord> = fold(mutableListOf<Coord>()) { acc, instruction ->
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

    fun part1() = intersections.minOf { it.manhattanDistance() }

    fun part2(): Int {
        return intersections.minOf { coord ->
            wiresInstructions.sumOf { it.indexOf(coord) + 1 }
        }
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D3(readRawInput("y2019/d3"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 266
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 19242
}