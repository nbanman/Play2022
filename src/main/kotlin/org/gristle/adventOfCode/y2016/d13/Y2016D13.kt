package org.gristle.adventOfCode.y2016.d13

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

// Refactored!

class Y2016D13(input: String) {
    private val favoriteNumber = input.toInt()
    private val start = Coord(1, 1)
    private val end = Coord(31, 39)

    private fun Coord.isOpen(): Boolean {
        val (x, y) = this
        if (x < 0 || y < 0) return false
        val num = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + favoriteNumber
        val ones = num.toString(2).count { it == '1' }
        return ones % 2 == 0
    }

    private val distances = Graph.bfs(start, { it == end }) { coord ->
        coord
            .getNeighbors()
            .filter { it.isOpen() }
    }

    fun part1() = distances.last().weight.toInt()

    fun part2() = distances.count { it.weight <= 50 }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D13(readRawInput("y2016/d13"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 92
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 124
}