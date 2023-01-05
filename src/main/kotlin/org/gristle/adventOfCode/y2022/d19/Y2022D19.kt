package org.gristle.adventOfCode.y2022.d19

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.getInts
import java.util.*
import kotlin.math.ceil

class Y2022D19(input: String) {

    sealed class Robots {
        data class Ore(val oreCost: Int) : Robots() {
            override fun timeUntilBuild(state: State): Int = if (oreCost <= state.ore) {
                1
            } else {
                ceil((oreCost - state.ore) / state.oreRobots.toFloat()).toInt() + 1
            }
        }

        data class Clay(val oreCost: Int) : Robots() {
            override fun timeUntilBuild(state: State): Int = if (oreCost <= state.ore) {
                1
            } else {
                ceil((oreCost - state.ore) / state.oreRobots.toFloat()).toInt() + 1
            }
        }

        data class Obsidian(val oreCost: Int, val clayCost: Int) : Robots() {
            override fun timeUntilBuild(state: State): Int = maxOf(
                if (oreCost <= state.ore) 1 else ceil((oreCost - state.ore) / state.oreRobots.toFloat()).toInt() + 1,
                if (clayCost <= state.clay) 1 else ceil((clayCost - state.clay) / state.clayRobots.toFloat()).toInt() + 1,
            )
        }

        data class Geode(val oreCost: Int, val obsidianCost: Int) : Robots() {
            override fun timeUntilBuild(state: State): Int = maxOf(
                if (oreCost <= state.ore) 1 else ceil((oreCost - state.ore) / state.oreRobots.toFloat()).toInt() + 1,
                if (obsidianCost <= state.obsidian) {
                    1
                } else {
                    ceil((obsidianCost - state.obsidian) / state.obsidianRobots.toFloat()).toInt() + 1
                },
            )
        }

        abstract fun timeUntilBuild(state: State): Int
    }

    data class Blueprint(
        val id: Int,
        val oreRobot: Robots.Ore,
        val clayRobot: Robots.Clay,
        val obsidianRobot: Robots.Obsidian,
        val geodeRobot: Robots.Geode,
    ) {
        val maxOreRobots = maxOf(
            oreRobot.oreCost,
            clayRobot.oreCost,
            obsidianRobot.oreCost,
            geodeRobot.oreCost,
        )
    }

    private val blueprints = input
        .getInts()
        .chunked(7)
        .map {
            Blueprint(
                it[0],
                Robots.Ore(it[1]),
                Robots.Clay(it[2]),
                Robots.Obsidian(it[3], it[4]),
                Robots.Geode(it[5], it[6])
            )
        }.toList()

    data class State(
        val blueprint: Blueprint,
        val minute: Int = 0,
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geodes: Int = 0,
        val oreRobots: Int = 1,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0,
        val parent: State? = null,
    ) : Comparable<State> {

        fun nextStates(minutes: Int): List<State> {
            val nextStates = mutableListOf<State>()
            var buildTime: Int
            // Ore
            if (oreRobots < blueprint.maxOreRobots) {
                buildTime = blueprint.oreRobot.timeUntilBuild(this)
                if (minutes - minute - buildTime >= 0) {
                    nextStates.add(
                        copy(
                            minute = minute + buildTime,
                            ore = ore - blueprint.oreRobot.oreCost + buildTime * oreRobots,
                            clay = clay + buildTime * clayRobots,
                            obsidian = obsidian + buildTime * obsidianRobots,
                            geodes = geodes + buildTime * geodeRobots,
                            oreRobots = oreRobots + 1,
                            parent = this,
                        )
                    )
                }
            }
            // Clay
            if (clayRobots < blueprint.obsidianRobot.clayCost) {
                buildTime = blueprint.clayRobot.timeUntilBuild(this)
                if (minutes - minute - buildTime >= 0) {
                    nextStates.add(
                        copy(
                            minute = minute + buildTime,
                            ore = ore - blueprint.clayRobot.oreCost + buildTime * oreRobots,
                            clay = clay + buildTime * clayRobots,
                            obsidian = obsidian + buildTime * obsidianRobots,
                            geodes = geodes + buildTime * geodeRobots,
                            clayRobots = clayRobots + 1,
                            parent = this,
                        )
                    )
                }
            }
            // Obsidian
            if (clayRobots > 0 && obsidianRobots < blueprint.geodeRobot.obsidianCost) {
                buildTime = blueprint.obsidianRobot.timeUntilBuild(this)
                if (minutes - minute - buildTime >= 0) {
                    nextStates.add(
                        copy(
                            minute = minute + buildTime,
                            ore = ore - blueprint.obsidianRobot.oreCost + buildTime * oreRobots,
                            clay = clay - blueprint.obsidianRobot.clayCost + buildTime * clayRobots,
                            obsidian = obsidian + buildTime * obsidianRobots,
                            geodes = geodes + buildTime * geodeRobots,
                            obsidianRobots = obsidianRobots + 1,
                            parent = this,
                        )
                    )
                }
            }

            // Geode
            if (obsidianRobots > 0) {
                buildTime = blueprint.geodeRobot.timeUntilBuild(this)
                if (minutes - minute - buildTime >= 0) {
                    nextStates.add(
                        copy(
                            minute = minute + buildTime,
                            ore = ore - blueprint.geodeRobot.oreCost + buildTime * oreRobots,
                            clay = clay + buildTime * clayRobots,
                            obsidian = obsidian - blueprint.geodeRobot.obsidianCost + buildTime * obsidianRobots,
                            geodes = geodes + buildTime * geodeRobots,
                            geodeRobots = geodeRobots + 1,
                            parent = this,
                        )
                    )
                }
            }

            return nextStates
        }

        fun maxBound(minutes: Int) = geodes + (0 until (minutes - minute)).sumOf { it + geodeRobots }

        fun minBound(minutes: Int) = geodes + (minutes - minute) * geodeRobots

        override fun compareTo(other: State): Int = compareBy<State> { it.geodes }
            .thenByDescending { it.minute }
            .compare(this, other)

        override fun toString(): String {
            return "State(minute=$minute, ore=$ore, clay=$clay, obsidian=$obsidian, geodes=$geodes, oreRobots=$oreRobots, clayRobots=$clayRobots, obsidianRobots=$obsidianRobots, geodeRobots=$geodeRobots)"
        }
    }

    private fun findGeodes(blueprint: Blueprint, minutes: Int): Int {
        var maxGeodes = 0
        val pq = PriorityQueue<State>(compareByDescending { it })
        pq.add(State(blueprint))
        while (pq.isNotEmpty()) {
            val state = pq.poll()
            if (state.maxBound(minutes) < maxGeodes) continue
            val minGeodes = state.minBound(minutes)
            if (minGeodes > maxGeodes) maxGeodes = minGeodes
            pq.addAll(state.nextStates(minutes))
        }
        return maxGeodes
    }

    suspend fun part1() = withContext(Dispatchers.Default) {
        blueprints.map { blueprint ->
            async {
                blueprint.id * findGeodes(blueprint, 24)
            }
        }.sumOf { it.await() }
    }

    suspend fun part2() = withContext(Dispatchers.Default) {
        blueprints.take(3).map { blueprint ->
            async { findGeodes(blueprint, 32) }
        }.fold(1) { acc, deferred -> acc * deferred.await() }
    }
}

suspend fun main() {
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
    val solver = Y2022D19(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 1427 (226ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 4400 (219ms)
    println("Total time: ${timer.elapsed()}ms") // 458ms
}