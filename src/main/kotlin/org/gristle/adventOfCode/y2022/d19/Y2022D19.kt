package org.gristle.adventOfCode.y2022.d19

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.getInts
import java.util.*

class Y2022D19(input: String) {

    sealed class Robot {
        data class OreRobot(val ore: Int)

        data class ClayRobot(val ore: Int)

        data class ObsidianRobot(val ore: Int, val clay: Int)

        data class GeodeRobot(val ore: Int, val obsidian: Int)
    }

    data class Blueprint(
        val id: Int,
        val oreRobot: Robot.OreRobot,
        val clayRobot: Robot.ClayRobot,
        val obsidianRobot: Robot.ObsidianRobot,
        val geodeRobot: Robot.GeodeRobot,
    )

    private val blueprints = input
        .getInts()
        .chunked(7)
        .map {
            Blueprint(
                it[0],
                Robot.OreRobot(it[1]),
                Robot.ClayRobot(it[2]),
                Robot.ObsidianRobot(it[3], it[4]),
                Robot.GeodeRobot(it[5], it[6])
            )
        }.toList()

    data class State(
        val blueprint: Blueprint,
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geodes: Int = 0,
        val oreRobots: Int = 1,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0
    ) : Comparable<State> {
        fun update() = copy(
            ore = ore + oreRobots,
            clay = clay + clayRobots,
            obsidian = obsidian + obsidianRobots,
            geodes = geodes + geodeRobots
        )

        override fun compareTo(other: State): Int {
            return if (geodeRobots != other.geodeRobots) geodeRobots - other.geodeRobots else {
                obsidianRobots - other.obsidianRobots
            }
        }
    }

    inline fun bfs(
        startId: State,
        minutes: Int,
        defaultEdges: (State) -> List<State> = { emptyList() }
    ): List<Graph.Vertex<State>> {
        val start = Graph.StdVertex(startId, 0.0)
        val q: Deque<Graph.Vertex<State>> = ArrayDeque()
        q.add(start)
        val visited = mutableMapOf(startId to start)
        while (q.isNotEmpty()) {
            val current = q.removeFirst()
            if (current.weight < minutes) {
                val edges = defaultEdges(current.id)
                edges
                    .filter {
                        it !in visited || visited.getValue(it).id < it
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

    val edges = { state: State ->
        val moves = mutableListOf(state).apply {
            if (state.ore >= state.blueprint.oreRobot.ore) add(state.copy(oreRobots = state.oreRobots + 1))
            if (state.ore >= state.blueprint.clayRobot.ore) add(state.copy(clayRobots = state.clayRobots + 1))
            if (state.ore >= state.blueprint.obsidianRobot.ore && state.clay >= state.blueprint.obsidianRobot.clay) {
                add(state.copy(obsidianRobots = state.obsidianRobots + 1))
            }
            if (state.ore >= state.blueprint.geodeRobot.ore && state.obsidian >= state.blueprint.geodeRobot.obsidian) {
                add(state.copy(geodeRobots = state.geodeRobots + 1))
            }
        }
        moves.map { it.update() }
    }

    fun part1() = blueprints.sumOf { blueprint ->
        val steps = bfs(
            startId = State(blueprint),
            minutes = 24,
            defaultEdges = edges
        )
        steps.maxOf { it.id.geodes } * blueprint.id
    }

    fun part2() = "To be implemented"
}

fun main() {
    val input = listOf(
        getInput(19, 2022),
        """Blueprint 1:
  Each ore robot costs 4 ore.
  Each clay robot costs 2 ore.
  Each obsidian robot costs 3 ore and 14 clay.
  Each geode robot costs 2 ore and 7 obsidian.

Blueprint 2:
  Each ore robot costs 2 ore.
  Each clay robot costs 3 ore.
  Each obsidian robot costs 3 ore and 8 clay.
  Each geode robot costs 3 ore and 12 obsidian.""",
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D19(input[1])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}