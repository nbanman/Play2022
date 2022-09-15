package org.gristle.adventOfCode.y2021.d15

import org.gristle.adventOfCode.utilities.*

object Y2021D15 {

    private val initialCavern = readRawInput("y2021/d15").toGrid { it.toDigit() }

    private fun solve(cavern: Grid<Int>): Int {
        val heuristic = { i: Int -> cavern.coordIndex(i).manhattanDistance(cavern.lastCoordIndex()).toDouble() }
        val defaultEdges = { i: Int ->
            cavern.getNeighborIndices(i).map { neighborIndex ->
                Graph.Edge(neighborIndex, cavern[neighborIndex].toDouble())
            }
        }
        val distances = Graph.aStar(startId = 0, heuristic = heuristic, defaultEdges = defaultEdges)
        return distances.last().weight.toInt()
    }

    fun part1() = solve(initialCavern)

    fun part2(): Int {

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
        return solve(expandedCavern)
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D15.part1()} (${elapsedTime(time)}ms)") // 602 (141ms Dij) (113 aStar)
    time = System.nanoTime()
    println("Part 2: ${Y2021D15.part2()} (${elapsedTime(time)}ms)") // 2935 (555ms Dij) (465 aStar)
}