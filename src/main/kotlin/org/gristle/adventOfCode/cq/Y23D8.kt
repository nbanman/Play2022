package org.gristle.adventOfCode.cq

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps

fun main() {
    val edges: Map<Int, List<Graph.Edge<Int>>> = input[0]
        .lineSequence()
        .mapIndexed { vertex, weightsString ->
            val weights = weightsString
                .split(' ')
                .mapIndexedNotNull { destination, weightString ->
                    val weight = weightString.toDouble()
                    if (vertex == destination) {
                        null
                    } else {
                        Graph.Edge(destination, weight)
                    }
                }
            vertex to weights
        }.toMap()

    val defaultEdges = { cities: List<Int> ->
        val filter: (Graph.Edge<Int>) -> Boolean = if (cities.size == edges.size) {
            { edge: Graph.Edge<Int> -> edge.vertexId == 0 }
        } else {
            { edge: Graph.Edge<Int> -> edge.vertexId !in cities }
        }
        edges
            .getValue(cities.last())
            .filter(filter)
            .map { Graph.Edge(cities + it.vertexId, it.weight) }
    }

    val steps = Graph
        .dijkstra(
            startId = listOf(0),
            endCondition = { it.size > edges.size },
            defaultEdges = defaultEdges
        ).steps()
    println(steps)
}

private val input = listOf(
    """0000 1971 2868 2102 2368 0420 2377 0629 1866 2419 2417 2249
1971 0000 0670 0367 0341 2651 1001 0703 2789 2090 2006 0350
2868 0670 0000 2594 2485 2199 0669 1466 2929 2256 0532 0742
2102 0367 2594 0000 0149 1051 2023 2156 0897 2633 0348 1490
2368 0341 2485 0149 0000 0706 1171 2274 1724 0748 0531 2708
0420 2651 2199 1051 0706 0000 1404 0865 1922 1977 2469 1499
2377 1001 0669 2023 1171 1404 0000 2118 2421 0263 1532 1686
0629 0703 1466 2156 2274 0865 2118 0000 2496 0426 2463 1633
1866 2789 2929 0897 1724 1922 2421 2496 0000 1868 1772 1943
2419 2090 2256 2633 0748 1977 0263 0426 1868 0000 1906 2683
2417 2006 0532 0348 0531 2469 1532 2463 1772 1906 0000 1937
2249 0350 0742 1490 2708 1499 1686 1633 1943 2683 1937 0000""",
    """00 10 15 20 08
10 00 05 11 14
15 05 00 08 15
20 11 08 00 12
08 14 15 12 00"""
)