package org.gristle.adventOfCode.y2016.d24

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

/**
 * Refactored; faster and cleaner!
 *
 * The original used BFS to find the distances between each number, found all the possible combinations for visiting
 * the numbers, calculated the total distance for each combination, and took the minimum. This is essentially the
 * same as a BFS for the second stage, which is far from optimal.
 *
 * I had an intermediary solution that just used BFS in one stage. It was very clean, but ran almost one second
 * slower than the OG version.
 *
 * My final version calculates naive weighted edges between all numbers using DFS, then feeds that into a Dijkstra
 * search. The "location" tracks not only the current position, but all numbers visited. This is enough information
 * to provide appropriate end conditions for both parts 1 and 2.
 */
class Y2016D24(input: String) {
    // Read map
    private val layout = input.toGrid()

    // Find the numbers in the map and associate it with their location
    private val numbers = layout.withIndex().filter { it.value.isDigit() }

    // Function to find neighbors for the upcoming DFS function.
    private val getNeighbors = { location: Int ->
        layout
            .getNeighborIndices(location)
            .filter { layout[it] != '#' }
    }

    // Naive edge map providing distance from any given number to all the other numbers. Naive in the sense that
    // the Dijkstra algo does not use it directly because we need to generate the edges on the fly in order to 
    // track the numbers already visited.
    private val edgeMap: Map<Char, List<Graph.Edge<Char>>> = numbers.associate { (location, number) ->
        // Obtain list of all distances from the number to every other number in the map
        val distances = Graph
            .dfs(location, defaultEdges = getNeighbors)
            .filter { layout[it.id].isDigit() }
        // Create map associating the number to a list of Edges with the other number and the distance.
        number to distances.drop(1).map { Graph.Edge(layout[it.id], it.weight) }
    }

    // "State" tracks where the search is currently at and what numbers have been visited.
    data class State(val location: Char, val numbersVisited: Set<Char>)

    // Both parts have the same start: at '0', thus having already visited '0'
    private val start = State('0', setOf('0'))

    // Function to plug into Dijkstra that takes the edges from the edgemap and massages them to include all
    // the State data.
    private val getEdges = { state: State ->
        edgeMap[state.location]
            ?.map { Graph.Edge(State(it.vertexId, state.numbersVisited + it.vertexId), it.weight) }
            ?: throw IllegalStateException("Dijkstra search reached location that is not in the edgemap.")
    }

    // Runs dijkstra and provides weight of shortest path. Takes in different end conditions to accommodate parts 1 & 2.
    fun solve(endCondition: (State) -> Boolean) = Graph.dijkstra(start, endCondition, defaultEdges = getEdges).steps()

    // Part one ends when all numbers have been visited
    fun part1() = solve { it.numbersVisited.size == numbers.size }

    // Part two ends when all numbers have been visited AND the robot has gone back to '0'
    fun part2() = solve { it.location == '0' && it.numbersVisited.size == numbers.size }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D24(readRawInput("y2016/d24"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 470 (218ms OG) (370ms BFS) (133ms 2-stage DFS-Dijk)
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 720 (36ms OG) (638ms BFS) (10ms 2-stage DFS-Dijk)
}