package org.gristle.adventOfCode.y2021.d11

import org.gristle.adventOfCode.utilities.*

object Y2021D11 {
    private val input = readRawInput("y2021/d11")

    val grid = input.toGrid().mapToGrid { Character.getNumericValue(it) }

    private var flashes = 0

    private fun Grid<Int>.takeStep(): Grid<Int> {
        fun Grid<Int>.flashIndices() = mapIndexedNotNull { index, i ->
            if (i >= 10) index else null
        }

        val incGrid = mapToMutableGrid { it + 1 }
        var flashIndices = incGrid.flashIndices()
        while (flashIndices.isNotEmpty()) {
            flashIndices.forEach { flashIndex ->
                val unflashedNeighbors = incGrid
                    .getNeighborIndices(flashIndex, true)
                    .filter { incGrid[it] != -1 }
                unflashedNeighbors.forEach { index ->
                    incGrid[index]++
                }
                incGrid[flashIndex] = -1
                flashes++
            }
            flashIndices = incGrid.flashIndices()
        }
        for (i in incGrid.indices) { incGrid[i] = incGrid[i].let { if (it == - 1) 0 else it } }
        return incGrid
    }

    fun part1(): Int {
        (1..100).fold(grid) { acc, _ ->
            val next = acc.takeStep()
            next
        }
        return flashes
    }

    fun part2(): Int {
        var counter = 0
        var inced = grid
        while (inced.any { it != 0 } ) {
            counter++
            inced = inced.takeStep()
        }
        return counter
    }

}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D11.part1()} (${elapsedTime(time)}ms)") // 1669
    time = System.nanoTime()
    println("Part 2: ${Y2021D11.part2()} (${elapsedTime(time)}ms)") // 351
}