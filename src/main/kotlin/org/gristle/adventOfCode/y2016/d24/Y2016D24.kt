package org.gristle.adventOfCode.y2016.d24

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.utilities.Graph.steps

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
 * I had another intermediary solution that calculated naive weighted edges between all numbers using DFS, then fed
 * that into a Dijkstra search. The "location" tracked not only the current position, but all numbers visited.
 * This was enough information to provide appropriate end conditions for both parts 1 and 2.
 *
 * My latest solution uses the Grid<Char>getEdgeMap function, which uses a bootstrapping Dijkstra to generate the
 * weighted edge map for all important locations. It is the same speed, but it is hopefully useful in the future.
 */

private typealias Location = IndexedValue<Char>
class Y2016D24(input: String) {
    // Read map
    private val layout = input.toGrid()

    // Find the numbers in the map and associate it with their location
    private val numbers = layout.withIndex().filter { it.value.isDigit() }

    // Naive edge map providing distance from any given number to all the other numbers. Naive in the sense that
    // the Dijkstra algo does not use it directly because we need to generate the edges on the fly in order to 
    // track the numbers already visited.    
    private val edgeMap = layout.getEdgeMap()

    // "State" tracks where the search is currently at and what numbers have been visited.
    data class State(val location: Location, val numbersVisited: Set<Location>)

    // Both parts have the same start: at '0', thus having already visited '0'
    private val start = Location(layout.indexOf('0'), '0').let { State(it, setOf(it)) }

    // Function to plug into Dijkstra that takes the edges from the edgemap and massages them to include all
    // the State data.
    private val getEdges = { state: State ->
        edgeMap[state.location]
            ?.map { Graph.Edge(State(it.vertexId, state.numbersVisited + it.vertexId), it.weight) }
            ?: throw IllegalStateException("Dijkstra search reached location that is not in the edgemap.")
    }

    // Runs dijkstra and provides weight of the shortest path. Takes in different end conditions to accommodate 
    // parts 1 & 2.
    fun solve(endCondition: (State) -> Boolean) = Graph.dijkstra(start, endCondition, defaultEdges = getEdges).steps()

    // Part one ends when all numbers have been visited
    fun part1() = solve { it.numbersVisited.size == numbers.size }

    // Part two ends when all numbers have been visited AND the robot has gone back to '0'
    fun part2() = solve { it.location.value == '0' && it.numbersVisited.size == numbers.size }
}

fun main() {
    val timer = Stopwatch(start = true)
    val c = Y2016D24(readRawInput("y2016/d24"))
//    val c = Y2016D24("""###########
//#0.1.....2#
//#.#######.#
//#4.......3#
//###########""")
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 470 (218ms OG) (370ms BFS) (133ms 2-stage DFS-Dijk)
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 720 (36ms OG) (638ms BFS) (10ms 2-stage DFS-Dijk)
    println("Total time: ${timer.elapsed()}ms") // 148ms
}