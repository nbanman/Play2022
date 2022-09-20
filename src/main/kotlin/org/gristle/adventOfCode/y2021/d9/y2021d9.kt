package org.gristle.adventOfCode.y2021.d9

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

object Y2021D9 {
    private val input = readRawInput("y2021/d9")

    private val heightMap = input.toGrid { Character.getNumericValue(it) }

    private val lowIndices = heightMap
        .indices
        .filter { index -> heightMap.getNeighbors(index).all { it > heightMap[index] } }

    fun part1() = lowIndices.sumOf { heightMap[it] + 1 }

    private fun Int.basinSize(): Int {
        val neighbors = { id: Int ->
            heightMap
                .getNeighborIndices(id)
                .filter { heightMap[it] != 9 && heightMap[it] > heightMap[id] }
        }
        return Graph.dfs(this, defaultEdges = neighbors).size
    }
    
    fun part2() = lowIndices
        .map { it.basinSize() }
        .sortedDescending()
        .take(3)
        .reduce { acc, i -> acc * i }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D9.part1()} (${elapsedTime(time)}ms)") // 448
    time = System.nanoTime()
    println("Part 2: ${Y2021D9.part2()} (${elapsedTime(time)}ms)") // 1417248
}