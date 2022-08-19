package org.gristle.adventOfCode.y2019.d18

import org.gristle.adventOfCode.utilities.*

// Refactored: much slower but much cleaner code. Not as fast because the previous version relied on 
// a custom Dijkstra algorithm that had a more effective cache to eliminate possible paths.

object Y2019D18 {
    class Edge(val node: Node, val weight: Int) {
        override fun toString(): String {
            return "E1918(node=${node.locator}, weight=$weight)"
        }
    }

    class Node(val locator: Int, val grid: Grid<Char>) {
        var safeToDelete = grid[locator] == '#'
        val edges = mutableListOf<Edge>()
        fun getEdges(nodes: List<Node>) {
            if (!safeToDelete) {
                grid.getNeighborIndices(locator)
                    .filter { grid[it] != '#' }
                    .map { Edge(nodes[it], 1) }
                    .apply { edges.addAll(this) }
            }
        }

        private fun replaceEdge(index: Int, edge: Edge) {
            val edgeIndex = edges.indexOfFirst { it.node.locator == index }
            val oldWeight = edges[edgeIndex].weight
            val newEdge = Edge(edge.node, edge.weight + oldWeight)
            edges[edgeIndex] = newEdge
        }

        private fun deleteEdge(index: Int) {
            val edgeIndex = edges.indexOfFirst { it.node.locator == index }
            edges.removeAt(edgeIndex)
        }

        fun safeDelete(): Boolean {
            if (safeToDelete) return true
            if (grid[locator] == '.') {
                return when (edges.size) {
                    1 -> {
                        edges[0].node.deleteEdge(locator)
                        safeToDelete = true
                        true
                    }
                    2 -> {
                        edges[0].node.replaceEdge(locator, edges[1])
                        edges[1].node.replaceEdge(locator, edges[0])
                        safeToDelete = true
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            return false
        }

        override fun toString(): String {
            return "Node(\'${grid[locator]}\', locator=$locator, safeToDelete=$safeToDelete, edges=${edges})"
        }
    }

    data class KeyEdge(val id: Char, val weight: Double, val keys: Set<Char>)

    data class State(val location: List<Char>, val keys: Set<Char>)

    private fun getNodes(grid: Grid<Char>): List<Node> {
        val unprunedNodes = List(grid.size) { index -> Node(index, grid) }
        with(unprunedNodes) {
            forEach { it.getEdges(this) }
            forEach { it.safeDelete() }
        }
        return unprunedNodes.filter { !it.safeToDelete }
    }

    private val input = readRawInput("y2019/d18")

    private val tunnels = input.toGrid()

    private const val robots = "@$%^"

    fun Grid<Char>.id(index: Int) = if (get(index) == '.') {
        index.toString()
    } else {
        get(index).toString()
    }

    fun solve(tunnels: Grid<Char>, starts: List<Char>): Int {
        // First step is to shrink the graph down, removing all vertices that are simply corridors with only 
        // two edges along the way to points of interest.
        val nodes = getNodes(tunnels)
        // Create a map of edges with this shrunken graph.
        @Suppress("RemoveExplicitTypeArguments") val edges = buildMap<String, List<Graph.Edge<String>>> {
            nodes.forEach { node ->
                put(
                    tunnels.id(node.locator),
                    node.edges.map { edge -> Graph.Edge(tunnels.id(edge.node.locator), edge.weight.toDouble()) })
            }
        }
        // Transform the map of edges to a form suitable for searching, with only keys being valid places to move
        // and each edge storing the keys needed to have been collected before.
        val keyEdges = edges
            .keys
            .filter { it[0].isLowerCase() || it[0] in robots }.associate { id ->
                id[0] to Graph
                    .dijkstra(id, edges = edges)
                    .filter { it.id[0].isLowerCase() }
                    .mapNotNull { distance ->
                        val path = distance.path().drop(1).filter { !it.id[0].isDigit() }
                        if (path.count { it.id[0].isLowerCase() } == 1) {
                            val keys = path
                                .filter { it.id[0].isUpperCase() }
                                .map { it.id[0].lowercaseChar() }
                                .toSet()
                            KeyEdge(path.last().id[0], path.last().weight, keys)
                        } else {
                            null
                        }
                    }
            }
        val startState = State(starts, emptySet())
        val endCondition = { state: State -> state.keys.size == 26 }
        val findEdges = { state: State ->
            state.location.flatMapIndexed { index, robot ->
                (keyEdges[robot] ?: emptyList())
                    .filter { state.keys.containsAll(it.keys) }
                    .map {
                        val newLocations = state.location.toMutableList().apply { this[index] = it.id }
                        val newState = State(newLocations, state.keys + it.id)
                        Graph.Edge(newState, it.weight)
                    }
            }
        }
        val solveDistance = Graph.dijkstra(startState, endCondition, defaultEdges = findEdges)
        return solveDistance.last().weight.toInt()
    }

    fun part1() = solve(tunnels, listOf('@'))

    fun part2(): Int {
        val quadrants = tunnels.toMutableGrid().apply {
            val originalStart = indexOf('@')
            val wallIndices = getNeighborIndices(originalStart)
            val newStartIndices = getNeighborIndices(originalStart, true) - wallIndices.toSet()
            this[originalStart] = '#'
            wallIndices.forEach { this[it] = '#' }
            newStartIndices.forEachIndexed { index, i -> this[i] = robots[index] }
        }.toGrid()
        return solve(quadrants, listOf('@', '$', '%', '^'))
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D18.part1()} (${elapsedTime(time)}ms)") // 3918 
    time = System.nanoTime()
    println("Part 2: ${Y2019D18.part2()} (${elapsedTime(time)}ms)") // 2004
}