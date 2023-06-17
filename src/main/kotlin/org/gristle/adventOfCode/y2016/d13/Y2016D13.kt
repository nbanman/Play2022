package org.gristle.adventOfCode.y2016.d13

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.isEven
import org.gristle.adventOfCode.utilities.isOdd

class Y2016D13(input: String) : Day {

    private val distances: List<Graph.Vertex<Coord>> by lazy {
        val favoriteNumber = input.toInt()
        val start = Coord(1, 1)
        val end = Coord(31, 39)
        val endCondition = { pos: Coord -> pos == end }

        fun Coord.isOpen(): Boolean {
            if (x < 0 || y < 0) return false
            val num = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + favoriteNumber
            val ones = generateSequence(num) { it.shr(1) }
                .takeWhile { it > 0 }
                .count(Int::isOdd)
            return ones.isEven()
        }
        
        val getEdges = { pos: Coord -> pos.getNeighbors().filter { it.isOpen() } }
        Graph.bfs(start, endCondition, defaultEdges = getEdges)
    }

    override fun part1() = distances.steps()

    override fun part2() = distances.count { it.weight <= 50 }
}

fun main() = Day.runDay(Y2016D13::class)

//    Class creation: 12ms
//    Part 1: 92 (10ms)
//    Part 2: 124 (0ms)
//    Total time: 23ms