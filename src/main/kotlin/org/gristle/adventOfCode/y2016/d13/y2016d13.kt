package org.gristle.adventOfCode.y2016.d13

import org.gristle.adventOfCode.utilities.*

// Refactored!

object Y2016D13 {
    private const val INPUT = 1350
    private val start = Coord(1, 1)
    private val end = Coord(31, 39)

    private fun Coord.isOpen(): Boolean {
        val (x, y) = this
        if (x < 0 || y < 0) return false
        val num = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + INPUT
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
    println("Part 1: ${Y2016D13.part1()} (${elapsedTime(time)}ms)") // 92
    time = System.nanoTime()
    println("Part 2: ${Y2016D13.part2()} (${elapsedTime(time)}ms)") // 124
}