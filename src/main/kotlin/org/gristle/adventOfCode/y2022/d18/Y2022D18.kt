package org.gristle.adventOfCode.y2022.d18

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.gristle.adventOfCode.utilities.*

class Y2022D18(input: String) {

    private val droplets = input.lines().map { line ->
        val (x, y, z) = line.getIntList()
        Xyz(x, y, z)
    }.toSet()

    private fun Xyz.adjacent() = listOf(
        Xyz(-1, 0, 0),
        Xyz(1, 0, 0),
        Xyz(0, -1, 0),
        Xyz(0, 1, 0),
        Xyz(0, 0, -1),
        Xyz(0, 0, 1),
    ).map { it + this }

    private fun getSurfaceArea(droplets: Set<Xyz>): Int {
        return droplets.sumOf { droplet ->
            val neighbors = droplet.adjacent()
            neighbors.count { !droplets.contains(it) }
        }
    }

    private suspend fun getRealSurfaceArea(droplets: Set<Xyz>): Int = withContext(Dispatchers.Default) {
        val xRange = droplets.minOf(Xyz::x)..droplets.maxOf(Xyz::x)
        val yRange = droplets.minOf(Xyz::y)..droplets.maxOf(Xyz::y)
        val zRange = droplets.minOf(Xyz::z)..droplets.maxOf(Xyz::z)
        val endCondition = { pos: Xyz -> pos.x !in xRange || pos.y !in yRange || pos.z !in zRange }
        val enclosed = mutableMapOf<Xyz, Boolean>()

        droplets.map { droplet ->
            async {
                val neighbors = droplet.adjacent()
                neighbors.count { neighbor ->
                    when {
                        droplets.contains(neighbor) -> false
                        enclosed[neighbor] == true -> false
                        endCondition(neighbor) -> true
                        else -> {
                            val bfs = Graph.bfs(
                                startId = neighbor,
                                endCondition = endCondition,
                                defaultEdges = { pos ->
                                    pos.adjacent().filterNot { droplets.contains(it) }
                                }
                            )
                            val isEnclosed = bfs.isEmpty()
                            bfs.forEach { enclosed[it.id] = isEnclosed }
                            !isEnclosed
                        }
                    }
                }
            }
        }.sumOf { it.await() }
    }

    fun part1() = getSurfaceArea(droplets)

    suspend fun part2() = getRealSurfaceArea(droplets)
}

fun main() = runBlocking {
    val input = getInput(18, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D18(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 4332 (39ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 2524 (1286ms)
    println("Total time: ${timer.elapsed()}ms") // 1394ms
}