package org.gristle.adventOfCode.y2022.d16

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.groupValues
import java.util.*

class Y2022D16(input: String) {
    private val flowMap = mutableMapOf<String, Int>()
    private val edgeMap = mutableMapOf<String, List<String>>()

    init {
        val pattern = """Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? (.*)""".toRegex()
        input.groupValues(pattern).forEach { gv ->
            val name = gv[0]
            val flowRate = gv[1].toInt()
            val tunnels = gv[2].split(", ")
            flowMap[name] = flowRate
            edgeMap[name] = tunnels
        }
    }

    data class State(val pos: Set<String>, val released: Int, val opened: Set<String> = emptySet()) {
        fun combine(el: State) = State(
            pos = pos + el.pos,
            released = released + el.released,
            opened = opened + el.opened
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (pos != other.pos) return false
            return true
        }

        override fun hashCode(): Int {
            var result = pos.hashCode()
            return result
        }
    }

    fun bfs(
        startId: State,
        minutes: Int,
        edges: (State, Int) -> List<State>
    ): List<Graph.Vertex<State>> {
        val start = Graph.StdVertex(startId, 0.0)
        val q: Deque<Graph.Vertex<State>> = ArrayDeque()
        q.add(start)
        val visited = mutableMapOf(startId to start)
        while (q.isNotEmpty()) {
            val current = q.removeFirst()
            if (current.weight < minutes) {
                val currentEdges = edges(current.id, minutes - current.weight.toInt() - 1)
                currentEdges
                    .filter {
                        val prev = visited[it]
                        if (prev == null) true else {
                            prev.id.released < it.released
                                    && prev.id.opened.intersect(it.opened).size == prev.id.opened.size
                        }

                    }
                    .map { Graph.StdVertex(it, current.weight + 1.0, current) }
                    .forEach { neighbor ->
                        visited[neighbor.id] = neighbor
                        q.add(neighbor)
                    }
            }
        }
        return visited.values.toList()
    }

    private val getEdges1 = { state: State, minutesRemaining: Int ->
        val me = state.pos.first()
        edgeMap.getValue(me).map { state.copy(pos = setOf(it)) } +
                if (state.opened.contains(me) || flowMap.getValue(me) == 0) emptyList() else
                    listOf(
                        state.copy(
                            released = state.released + minutesRemaining * flowMap.getValue(me),
                            opened = state.opened + me
                        )
                    )
    }

    private val getEdges2 = { state: State, minutesRemaining: Int ->
        val me = state.pos.first()
        val myMoves = edgeMap.getValue(me).map { state.copy(pos = setOf(it)) } +
                if (state.opened.contains(me) || flowMap.getValue(me) == 0) emptyList() else
                    listOf(
                        State(
                            setOf(me),
                            state.released + minutesRemaining * flowMap.getValue(me),
                            state.opened + me
                        )
                    )
        val el = state.pos.last()
        val elMoves = edgeMap.getValue(el).map { State(setOf(it), 0, emptySet()) } +
                if (state.opened.contains(el) || flowMap.getValue(me) == 0 || me == el) emptyList() else
                    listOf(state.copy(released = minutesRemaining * flowMap.getValue(el), opened = setOf(el)))
        elMoves.flatMap { elMove -> myMoves.map { myMove -> myMove.combine(elMove) } }.distinct()
    }

    fun solve(minutes: Int, getEdges: (State, Int) -> List<State>): Int {
        val combos = bfs(State(setOf("AA"), 0, emptySet()), minutes, getEdges)
        return combos.maxOf { it.id.released }
    }

    fun part1() = solve(30, getEdges1)

    fun part2() = solve(26, getEdges2)
}

fun main() {
    val input = listOf(
        getInput(16, 2022),
        """Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II""",
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D16(input[1])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 2059 (344ms) (1651 example 104ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // (1707 example)
    println("Total time: ${timer.elapsed()}ms")
}