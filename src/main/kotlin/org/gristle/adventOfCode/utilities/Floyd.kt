package org.gristle.adventOfCode.utilities

/**
 * Not yet working!
 */
fun <E> floyd(
    graph: Map<E, List<Graph.Edge<E>>> = mapOf(),
): Map<E, Map<E, List<Graph.Edge<E>>>> {
    val lookup = graph.entries.withIndex().associate { (index, entry) -> entry.key to index }
    val reverseLookup = graph.map { (key, _) -> key }
    val dist = MutableGrid(graph.size, graph.size) { Double.POSITIVE_INFINITY }
    val prev = MutableGrid<Int?>(graph.size, graph.size) { null }

    graph.forEach { (node, edges) ->
        edges.forEach { edge ->
            dist[lookup.getValue(edge.vertexId), lookup.getValue(node)] = edge.weight
            prev[lookup.getValue(edge.vertexId), lookup.getValue(node)] = lookup[edge.vertexId]
        }
        dist[lookup.getValue(node), lookup.getValue(node)] = 0.0
        prev[lookup.getValue(node), lookup.getValue(node)] = lookup.getValue(node)
    }

    for (k in 0 until graph.size) {
        for (i in 0 until graph.size) {
            for (j in 0 until graph.size) {
//                    println("k: $k, i: $i, j: $j, dist[j, i]: ${dist[j, i]}, dist[k, i]: ${dist[k, i]}, dist[j, k]: ${dist[j, k]}")
                if (dist[j, i] > dist[k, i] + dist[j, k]) {
//                        print("dist[$j, $i] changed from ${dist[j, i]}")
                    dist[j, i] = dist[k, i] + dist[j, k]
                    prev[j, i] = prev[k, i]
//                        println(" to ${dist[j, i]}")
                }
            }
        }
    }

    return buildMap<E, MutableMap<E, MutableList<Graph.Edge<E>>>> {
        prev
            .chunked(graph.size)
            .forEachIndexed { startIndex, nextOnes ->
                nextOnes.forEachIndexed { endIndex, _ ->
                    val path = getOrPut(reverseLookup[startIndex]) { mutableMapOf() }
                        .getOrPut(reverseLookup[endIndex]) { mutableListOf() }
                    var u = startIndex
                    while (u != endIndex) {
                        val weight = dist[u]
                        u = prev[endIndex, u] ?: throw IllegalArgumentException("Null found at $endIndex, $u")
                        path.add(Graph.Edge(reverseLookup[u], weight))
                    }
                }
            }

    }
}