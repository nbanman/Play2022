package org.gristle.adventOfCode.y2021.d15

import org.gristle.adventOfCode.utilities.*

object Y2021D15 {

    private val initialCavern = readRawInput("y2021/d15").toGrid { it.toDigit() }

    /**
     * Uses A* algorithm to find the shortest path from the start (index 0) to the end (lastIndex of Grid).
     * The locator id is the index in the Grid.
     * The heuristic is manhattan distance from the end.
     * The end position is implicitly determined by the heuristic function returning 0.
     * Rather than supply a graph, the defaultEdges function supplies edges on demand.
     */
    private fun shortestPath(cavern: Grid<Int>): Int {
        val heuristic = { i: Int -> cavern.coordOf(i).manhattanDistance(cavern.lastCoord()).toDouble() }
        val defaultEdges = { i: Int -> cavern.getNeighborIndices(i).map { Graph.Edge(it, cavern[it].toDouble()) } }
        return Graph.aStar(startId = 0, heuristic = heuristic, defaultEdges = defaultEdges)
            .last() // aStar function provides list of all nodes in path, so it's last one we care about
            .weight
            .toInt()
    }

    fun part1() = shortestPath(initialCavern)

    fun part2(): Int {
        // Adds *n* to the existing risk, with 10 rolling back to 1.
        fun Int.addRisk(n: Int) = (this + n - 1) % 9 + 1

        val expandedCavern = initialCavern
            .addRight(initialCavern.mapToGrid { it.addRisk(1) })
            .addRight(initialCavern.mapToGrid { it.addRisk(2) })
            .addRight(initialCavern.mapToGrid { it.addRisk(3) })
            .addRight(initialCavern.mapToGrid { it.addRisk(4) })
            .let { fatCavern ->
                fatCavern
                    .addDown(fatCavern.mapToGrid { it.addRisk(1) })
                    .addDown(fatCavern.mapToGrid { it.addRisk(2) })
                    .addDown(fatCavern.mapToGrid { it.addRisk(3) })
                    .addDown(fatCavern.mapToGrid { it.addRisk(4) })
            }

        return shortestPath(expandedCavern)
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D15.part1()} (${elapsedTime(time)}ms)") // 602 (141ms Dij) (113 aStar)
    time = System.nanoTime()
    println("Part 2: ${Y2021D15.part2()} (${elapsedTime(time)}ms)") // 2935 (555ms Dij) (465 aStar)
}