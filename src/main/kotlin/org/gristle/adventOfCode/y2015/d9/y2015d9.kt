package org.gristle.adventOfCode.y2015.d9

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

object Y2015D9 {
    private val input = readRawInput("y2015/d9")

    private val edgeMap = buildMap<String, MutableList<Graph.Edge<String>>> {
        input.groupValues("""(\w+) to (\w+) = (\d+)""").forEach { gv ->
            computeIfAbsent(gv[0]) { mutableListOf() }.add(Graph.Edge(gv[1], gv[2].toDouble()))
            computeIfAbsent(gv[1]) { mutableListOf() }.add(Graph.Edge(gv[0], gv[2].toDouble()))
        }
    }

    data class Leg(val startCity: String, val endCity: String, val distance: Int)

    private val legs = input
        .groupValues("""(\w+) to (\w+) = (\d+)""")
        .flatMap { gv ->
            listOf(
                Leg(gv[0], gv[1], gv[2].toInt()),
                Leg(gv[1], gv[0], gv[2].toInt())
            )
        }

    private val cities = edgeMap.keys

    data class State(val location: String, val visited: Set<String>)

    private fun longestDistance(
        legs: List<Leg>,
        remainingLegs: List<Leg>,
        remainingCities: List<String>,
        distance: Int = 0
    ): Int {
        if (remainingCities.size <= 1) return distance
        val matchingLegs = if (legs.isEmpty()) {
            remainingLegs
        } else {
            remainingLegs.filter { leg ->
                leg.startCity == legs.last().endCity && remainingCities.contains(leg.endCity)
            }
        }
        return matchingLegs.maxOf { leg ->
            longestDistance(
                legs + leg,
                (remainingLegs - leg).filter { remainingLeg ->
                    remainingCities.contains(remainingLeg.endCity) && remainingLeg.endCity != leg.endCity
                },
                remainingCities - leg.startCity,
                distance + leg.distance
            )
        }
    }

    private val defaultEdges = { id: State ->
        edgeMap[id.location]
            ?.filter { !id.visited.contains(it.vertexId) }
            ?.map { Graph.Edge(State(it.vertexId, id.visited + it.vertexId), it.weight) }
            ?: emptyList()
    }

    fun part1() = cities.minOf { city ->
        Graph.dijkstra(
            startId = State(city, setOf(city)),
            endCondition = { id -> id.visited == cities },
            defaultEdges = defaultEdges
        ).last().weight.toInt()
    }

    fun part2() = longestDistance(emptyList(), legs, cities.toList())
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D9.part1()} (${elapsedTime(time)}ms)") // 207 (79ms Dij) (208ms old)
    time = System.nanoTime()
    println("Part 2: ${Y2015D9.part2()} (${elapsedTime(time)}ms)") // 804 (140ms)
}