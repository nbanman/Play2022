package org.gristle.adventOfCode.y2022.d18

import org.gristle.adventOfCode.utilities.*

class Y2022D18(input: String) {

    private val bounds: Triple<IntRange, IntRange, IntRange>
    private val droplets: Set<Xyz>

    init {
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE
        var minZ = Int.MAX_VALUE
        var maxZ = Int.MIN_VALUE

        droplets = input.getInts().chunked(3).map { (x, y, z) ->
            if (x < minX) minX = x
            if (x > maxX) maxX = x
            if (y < minY) minY = y
            if (y > maxY) maxY = y
            if (z < minZ) minZ = z
            if (z > maxZ) maxZ = z
            Xyz(x, y, z)
        }.toSet()

        bounds = Triple(minX - 1..maxX + 1, minY - 1..maxY + 1, minZ - 1..maxZ + 1)
    }

    private fun Xyz.adjacent() = Xyz.CROSS.map { it + this }

    private inline fun solve(predicate: (Xyz) -> Boolean): Int = droplets.sumOf { droplet ->
        droplet.adjacent().count { predicate(it) }
    }

    fun part1() = solve { it !in droplets }

    fun part2(): Int {
        val exterior = Graph
            .bfs(
                startId = Xyz(bounds.first.first, bounds.second.first, bounds.third.first),
                defaultEdges = { pos ->
                    pos.adjacent().filter {
                        it.x in bounds.first
                                && it.y in bounds.second
                                && it.z in bounds.third
                                && !droplets.contains(it)
                    }
                }
            ).map { it.id }
            .toSet()

        return solve { it in exterior }
    }
}

fun main() {
    val input = getInput(18, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D18(input)
    println("Class creation: ${timer.lap()}ms") // 58ms
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 4332 (40ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 2524 (80ms) (1286ms original)
    println("Total time: ${timer.elapsed()}ms") // 179ms
}