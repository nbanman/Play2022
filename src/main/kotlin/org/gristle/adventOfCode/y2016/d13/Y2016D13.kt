package org.gristle.adventOfCode.y2016.d13

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps

class Y2016D13(input: String) : Day {
    private val favoriteNumber = input.toInt()
    private fun Coord.isOpen(): Boolean {
        if (x < 0 || y < 0) return false
        val num = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + favoriteNumber
        val ones = generateSequence(num) { it.shr(1) }
            .takeWhile { it > 0 }
            .count { it and 1 == 1 }
        return ones and 1 == 0
    }

    private val start = Coord(1, 1)
    private val endCondition = { pos: Coord -> pos == Coord(31, 39) }
    private val getEdges = { pos: Coord -> pos.getNeighbors().filter { it.isOpen() } }
    private val distances = Graph.bfs(start, endCondition, defaultEdges = getEdges)

    override fun part1() = distances.steps()

    override fun part2() = distances.count { it.weight <= 50 }
}

fun main() = Day.runDay(Y2016D13::class) // 92, 124