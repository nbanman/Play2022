package org.gristle.adventOfCode.y2022.d16

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.pollUntil
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Y2022D16(input: String) : Day {
    val pattern = """Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? (.*)""".toRegex()
    private val flowMap = mutableMapOf<String, Int>()
    private val edgeMapNoValves = mutableMapOf<String, List<Graph.Edge<String>>>()
    private val edgeMap: Map<String, Map<String, Int>>

    init {
        // parse to edgeMapNoValves and flowRate maps
        input.groupValues(pattern).forEach { gv ->
            val name = gv[0]
            val flowRate = gv[1].toInt()
            val tunnels = gv[2].split(", ").map { Graph.Edge(it, 1.0) }
            flowMap[name] = flowRate
            edgeMapNoValves[name] = tunnels
        }
        // get point to point info on all the points, getting rid of 0-flow valve locations
        edgeMapNoValves
            .keys
            .filter { it == "AA" || flowMap.getValue(it) > 0 }
            .forEach { valve ->
                edgeMapNoValves[valve] = Graph.dijkstra(valve, edges = edgeMapNoValves)
                    .filter { it.id != "AA" && flowMap.getValue(it.id) > 0 && it.weight != 0.0 }
                    .map { Graph.Edge(it.id, it.weight) }
            }
        // clean up the map
        edgeMap = edgeMapNoValves
            .filter { it.key == "AA" || flowMap.getValue(it.key) > 0 }
            .map { it.key to it.value.associate { edge -> edge.vertexId to edge.weight.toInt() + 1 } }
            .associate { it }
    }

    /**
     * Tracks everything. Note that total can be negative to avoid double-counting things that do not happen until
     * the other agent moves.
     */
    data class State(
        val pos: List<Pair<String, Int>>,
        val valves: Set<String>,
        val flow: Int,
        val total: Int,
        val minute: Int,
    )

    fun solve(agents: Int, minutes: Int): Int {
        var max = 0

        val startId = State(
            pos = List(agents) { "AA" to 0 },
            valves = edgeMap.getValue("AA").keys,
            flow = 0,
            total = 0,
            minute = minutes + 1,
        )

        val open = PriorityQueue<Pair<State, Int>>(compareByDescending { it.second })
        open.add(startId to 0)

        val closed = mutableSetOf<State>()

        while (open.isNotEmpty()) {
            val (current, heuristic) = open.pollUntil { !closed.contains(it.first) } ?: break
            val firstRoom = current.pos.first()
            val secondRoom = current.pos.last()
            closed.add(current)

            val potentialFuture = heuristic + current.valves.sumOf { valve ->
                val flowRate = flowMap.getValue(valve)
                current.pos.maxOf { (room, timeOffset) ->
                    flowRate * max(0, (current.minute + timeOffset - edgeMap.getValue(room).getValue(valve)))
                }
            }

            if (heuristic > max) max = heuristic

            if (potentialFuture < max) continue

            val distanceMap = current.valves.associateWith { valve ->
                current
                    .pos
                    .mapIndexed { roomNo, (room, timeOffset) ->
                        roomNo to edgeMap.getValue(room).getValue(valve) - timeOffset
                    }.minBy { it.second }
            }

            val newStates = distanceMap.map { (valve, roomInfo) ->
                val (roomNo, distance) = roomInfo
                val newPos = run {
                    if (current.pos.size == 1) {
                        listOf(valve to 0)
                    } else {
                        if (roomNo == 0) { // first room moving
                            val newFirstOffset = min(0, distance)
                            val newSecondOffset = max(0, distance)
                            listOf(
                                valve to newFirstOffset,
                                secondRoom.first to newSecondOffset
                            )
                        } else {
                            val newFirstOffset = max(0, distance)
                            val newSecondOffset = -min(0, distance)
                            listOf(
                                firstRoom.first to newFirstOffset,
                                valve to newSecondOffset
                            )
                        }
                    }
                }
                val newFlow = current.flow + flowMap.getValue(valve)
                val newMinute = min(current.minute - distance, current.minute)
                val newState = State(
                    pos = newPos,
                    valves = current.valves - valve,
                    flow = newFlow,
                    total = current.total + current.flow * distance,
                    minute = newMinute
                )
                val newHeuristic = heuristic + flowMap.getValue(valve) * (newMinute - 1)

                newState to newHeuristic
            }
            open.addAll(newStates)
        }

        return max
    }

    override fun part1() = solve(1, 30)

    override fun part2() = solve(2, 26)
}

fun main() = Day.runDay(16, 2022, Y2022D16::class) // 2059 (121ms), 2790 (5337ms)