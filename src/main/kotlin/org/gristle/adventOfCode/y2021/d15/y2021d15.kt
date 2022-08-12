package org.gristle.adventOfCode.y2021.d15

import org.gristle.adventOfCode.utilities.*

object Y2021D15 {
    private val input = readRawInput("y2021/d15")

    val cavern = input.toGrid().mapToGrid { it.toDigit() }

    fun solve(cavern: Grid<Int>): Int {
        val d = Graph.dijkstra(0, { it == cavern.lastIndex }) {
            cavern.getNeighborIndices(it).map { neighborIndex ->
                Graph.Edge(neighborIndex, cavern[neighborIndex].toDouble())
            }
        }
        return d.last().weight.toInt()
    }

    fun part1() = solve(cavern)

    fun part2(): Int {

        fun Int.addRisk(n: Int) = (this + n).let {
            if (it > 9) it - 9 else it
        }

        val expandedCavern = cavern
            .addRight(cavern.mapToGrid { it.addRisk(1) })
            .addRight(cavern.mapToGrid { it.addRisk(2) })
            .addRight(cavern.mapToGrid { it.addRisk(3) })
            .addRight(cavern.mapToGrid { it.addRisk(4) })
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
    println("Part 1: ${Y2021D15.part1()} (${elapsedTime(time)}ms)") // 602
    time = System.nanoTime()
    println("Part 2: ${Y2021D15.part2()} (${elapsedTime(time)}ms)") // 2935
}