package org.gristle.adventOfCode.y2021.d9

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

class Y2021D9(input: String) {
    private val heightMap = input.toGrid(Character::getNumericValue)

    private val lowIndices = heightMap
        .indices
        .filter { index -> heightMap.getNeighbors(index).all { it > heightMap[index] } }

    fun part1() = lowIndices.sumOf { heightMap[it] + 1 }

    // Edge finder for below BFS
    private val neighbors = { id: Int ->
        heightMap
            .getNeighborIndices(id)
            .filter { heightMap[it] != 9 && heightMap[it] > heightMap[id] }
    }

    fun part2() = lowIndices
        .map { Graph.bfs(startId = it, defaultEdges = neighbors).size } // for each low point get size of each basin
        .sortedDescending() // sort by largest first
        .take(3) // take top 3
        .reduce(Int::times) // multiply them together
}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D9(readRawInput("y2021/d9"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 448
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1417248
}