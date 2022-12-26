package org.gristle.adventOfCode.y2022.d18

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

    fun getSurfaceArea(droplets: Set<Xyz>): Int {
        return droplets.sumOf { droplet ->
            val neighbors = droplet.adjacent()
            neighbors.count { !droplets.contains(it) }
        }
    }

    fun getRealSurfaceArea(droplets: Set<Xyz>): Int {
        val xRange = droplets.minOf(Xyz::x)..droplets.maxOf(Xyz::x)
        val yRange = droplets.minOf(Xyz::y)..droplets.maxOf(Xyz::y)
        val zRange = droplets.minOf(Xyz::z)..droplets.maxOf(Xyz::z)
        val endCondition = { pos: Xyz -> pos.x !in xRange || pos.y !in yRange || pos.z !in zRange }
        val enclosed = mutableMapOf<Xyz, Boolean>()

        return droplets.sumOf { droplet ->
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
    }

    fun part1() = getSurfaceArea(droplets)

    fun part2() = getRealSurfaceArea(droplets)
}

fun main() {
    val input = listOf(
        getInput(18, 2022),
        """2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5""",
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D18(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 4332
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 2524
    println("Total time: ${timer.elapsed()}ms")
}