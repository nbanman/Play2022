package org.gristle.adventOfCode.y2019.d18

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.toGrid
import org.gristle.adventOfCode.utilities.toMutableGrid

// Refactored: much slower but much cleaner code. Not as fast because the previous version relied on 
// a custom Dijkstra algorithm that had a more effective cache to eliminate possible paths.

class Y2019D18(input: String) : Day {

    data class Key(val currentKey: Char, val precedingKeys: Set<Char>)

    data class State(val location: List<Char>, val keys: Set<Char>)

    private val tunnels = input.toGrid()

    fun solve(tunnels: Grid<Char>, starts: List<Char>): Int {
        // Intermediate edge map used to later make the keyEdges edge map used for the final solution.
        val edges: Map<Char, List<Graph.Edge<Char>>> = buildMap {
            tunnels // take input...
                .withIndex() // associate each value with its index...
                .filter { (_, value) -> value.isLetter() || value in starts } // only run for important positions
                .forEach { (index, start) ->

                    // lambda for getting edges from the grid
                    val getEdges: (IndexedValue<Char>) -> List<IndexedValue<Char>> = { (index, _) ->
                        tunnels
                            .getNeighborsIndexedValue(index) // get adjacent values
                            .filter { it.value != '#' } // remove walls
                    }

                    // populate edge map with all key and door locations that do not require passing through a 
                    // different key or door to get to
                    this[start] = Graph.bfs(IndexedValue(index, start), defaultEdges = getEdges)
                        .drop(1)
                        .filter { it.id.value.isLetter() } // only look at keys or doors
                        .filter { destination -> // this checks that there are no other keys or doors in between
                            destination
                                .path()
                                .let { path -> path.subList(1, path.lastIndex) }
                                .all { it.id.value == '.' }
                        }
                        .map { Graph.Edge(it.id.value, it.weight) } // convert to an Edge
                }
        }

        // Transform the map of edges to a form suitable for searching, with only keys being valid places to move
        // and each edge storing the keys needed to have been collected before.
        val keyEdges: Map<Char, List<Graph.Edge<Key>>> = edges
            .keys
            .filter { it.isLowerCase() || it in starts }
            .associateWith { key ->
                Graph
                    .dijkstra(key, edges = edges)
                    .filter { it.id.isLowerCase() }
                    .mapNotNull { distance ->
                        val path = distance.path().drop(1).filter { it.id.isLetter() }
                        if (path.count { it.id.isLowerCase() } == 1) {
                            val keys = path
                                .filter { it.id.isUpperCase() }
                                .map { it.id.lowercaseChar() }
                                .toSet()
                            Graph.Edge(Key(path.last().id, keys), path.last().weight)
                        } else {
                            null
                        }
                    }
            }

        val startState = State(starts, emptySet())
        val endCondition = { state: State -> state.keys.size == 26 }

        // lambda finds edges by looking at each robot position, looking where each could move, and generating
        // new states for each possible move
        val findEdges = { state: State ->
            state.location.flatMapIndexed { robot, location ->
                keyEdges
                    .getValue(location)
                    .filter { state.keys.containsAll(it.vertexId.precedingKeys) }
                    .map { edge ->
                        val newLocations: List<Char> =
                            state.location.toMutableList().apply { this[robot] = edge.vertexId.currentKey }
                        val newState = State(newLocations, state.keys + edge.vertexId.currentKey)
                        Graph.Edge(newState, edge.weight)
                    }
            }
        }
        return Graph.dijkstra(startState, endCondition, defaultEdges = findEdges).steps()
    }

    override fun part1() = solve(tunnels, listOf('@'))

    override fun part2(): Int {
        val robots = "@$%^".toList()

        // Change the grid so that there are 4 quadrants
        val quadrants: Grid<Char> = tunnels.toMutableGrid().apply {
            val originalStart = indexOf('@')
            val wallIndices = getNeighborIndices(originalStart)
            val newStartIndices = getNeighborIndices(originalStart, true) - wallIndices.toSet()
            this[originalStart] = '#'
            wallIndices.forEach { this[it] = '#' }
            newStartIndices.forEachIndexed { index, i -> this[i] = robots[index] }
        }
        return solve(quadrants, robots)
    }
}

fun main() = Day.runDay(Y2019D18::class)

//    [2019 Day 18]
//    Class creation: 18ms
//    Part 1: 3918 (940ms)
//    Part 2: 2004 (3453ms)
//    Total time: 4412ms