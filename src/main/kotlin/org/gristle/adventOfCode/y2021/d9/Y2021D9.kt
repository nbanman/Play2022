package org.gristle.adventOfCode.y2021.d9

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.toGrid

class Y2021D9(input: String) : Day {
    private val heightMap = input.toGrid(Char::digitToInt)

    private val lowIndices = heightMap
        .indices
        .filter { index -> heightMap.getNeighbors(index).all { it > heightMap[index] } }

    override fun part1() = lowIndices.sumOf { heightMap[it] + 1 }

    override fun part2(): Int {
        // Edge finder for below BFS
        val neighbors = { id: Int ->
            heightMap
                .getNeighborIndices(id)
                .filter { heightMap[it] != 9 && heightMap[it] > heightMap[id] }
        }

        return lowIndices
            .map { Graph.bfs(startId = it, defaultEdges = neighbors).size } // for each low point get size of each basin
            .sortedDescending() // sort by largest first
            .take(3) // take top 3
            .reduce(Int::times) // multiply them together
    }
}

fun main() = Day.runDay(Y2021D9::class)

//    Class creation: 39ms
//    Part 1: 448 (0ms)
//    Part 2: 1417248 (27ms)
//    Total time: 67ms