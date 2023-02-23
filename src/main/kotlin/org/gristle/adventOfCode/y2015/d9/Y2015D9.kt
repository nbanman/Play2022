package org.gristle.adventOfCode.y2015.d9

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.takeUntil

class Y2015D9(private val input: String) : Day {

    // Parse input into adjacency map for Dijkstra search.
    private val edgeMap = buildMap<String, MutableList<Graph.Edge<String>>> {
        input.groupValues("""(\w+) to (\w+) = (\d+)""").forEach { gv ->
            getOrPut(gv[0]) { mutableListOf() }.add(Graph.Edge(gv[1], gv[2].toDouble()))
            getOrPut(gv[1]) { mutableListOf() }.add(Graph.Edge(gv[0], gv[2].toDouble()))
        }
    } as Map<String, List<Graph.Edge<String>>>

    // The edgemap keys are a set of all the cities on the graph.
    private val cities = edgeMap.keys

    // The Dijkstra search does not use the adjacency map directly because the state object being tracked is the
    // complete list of cities, and repeat city visits are not allowed. This function translates the lookup and
    // applies the rule.
    private val defaultEdges = { id: List<String> ->
        edgeMap[id.last()] // grabs edges connected to last city visited
            ?.filter { !id.contains(it.vertexId) } // filters out adjacent cities that have already been visited
            ?.map { Graph.Edge(id + it.vertexId, it.weight) } // provides an edge using the adjacency list data
            ?: throw IllegalStateException("City does not exist.")
    }

    private fun tour(city: String) = Graph.dijkstraSequence(startId = listOf(city), defaultEdges = defaultEdges)

    // Part one finds the shortest path so the end condition is specified with takeUntil. It runs from each city
    // (hmm... Floyd-Warshall?) then takes the minimum of each city.
    override fun part1() = cities.minOf { city ->
        tour(city)
            .takeUntil { it.id.size == cities.size }
            .steps()
    }

    // Part two is the same as Part one except that it finds the longest path so no end condition is specified.
    // Instead, it runs until there are no more nodes to visit, then grabs the largest weight.
    override fun part2() = cities.maxOf { city ->
        tour(city)
            .filter { it.id.size == cities.size }
            .maxOf { it.weight.toInt() }
    }
}

// Pt1: 207 (27ms Dij) (156ms old)
// Pt2: 804 (171ms Dij) (140ms old)
fun main() = Day.runDay(9, 2015, Y2015D9::class)