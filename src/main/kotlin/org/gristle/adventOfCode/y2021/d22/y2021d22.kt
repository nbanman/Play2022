package org.gristle.adventOfCode.y2021.d22

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

object Y2021D22 {
    private val input = readRawInput("y2021/d22")

    val pattern = """(on|off) x=(-?\d+)\.\.(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""".toRegex()

    data class Cuboid(val turnOn: Boolean, val x: IntRange, val y: IntRange, val z: IntRange) {

        private val ranges = listOf(x, y, z)

        fun volume() = x.count().toLong() * y.count() * z.count()

        private fun IntRange.union(other: IntRange) =
            if (last >= other.first && first <= other.last) {
                kotlin.math.max(first, other.first)..kotlin.math.min(last, other.last)
            } else null

        operator fun plus(other: Cuboid) = volume() + other.volume() - (intersect(other)?.volume() ?: 0)

        operator fun minus(other: Cuboid) = volume() - (intersect(other)?.volume() ?: 0)

        private fun intersect(other: Cuboid): Cuboid? {
            return x.union(other.x)?.let { xU ->
                y.union(other.y)?.let { yU ->
                    z.union(other.z)?.let { zU ->
                        Cuboid(true, xU, yU, zU)
                    }
                }
            }
        }

        private fun IntRange.disjointRanges(other: IntRange): List<IntRange> {
            val disjoints = mutableListOf<IntRange>()
            if (other.first < first) disjoints.add(other.first until first)
            if (other.last > last) disjoints.add((last + 1)..other.last)
            return disjoints
        }

        fun disjointCubes(other: Cuboid): List<Cuboid> {
            val overlap = intersect(other) ?: return listOf(other)
            return  mutableListOf<Cuboid>().apply {
                addAll(x.disjointRanges(other.x).map { Cuboid(true, it, other.y, other.z) })
                addAll(y.disjointRanges(other.y).map { Cuboid(true, overlap.x, it, other.z) })
                addAll(z.disjointRanges(other.z).map { Cuboid(true, overlap.x, overlap.y, it) })
            }
        }

        fun inRange(range: IntRange) = ranges.all { it.first >= range.first && it.last <= range.last }
    }

    private val cuboids = input
        .groupValues(pattern)
        .map { gv ->
            val turnOff = gv[0] == "on"
            val nv = gv.mapNotNull { it.toIntOrNull() }
            Cuboid(turnOff, nv[0]..nv[1], nv[2]..nv[3], nv[4]..nv[5])
        }

    private fun findCubes(cuboids: List<Cuboid>): Long {
        var visited = mutableListOf<Cuboid>()
        cuboids.forEach { cuboid ->
            visited = visited.flatMap { prior -> cuboid.disjointCubes(prior) }.toMutableList()
            if (cuboid.turnOn) visited.add(cuboid)
        }
        return visited.sumOf { if (it.turnOn) it.volume() else 0L }
    }

    fun part1() = findCubes(cuboids.filter { it.inRange(-50..50) })

    fun part2() = findCubes(cuboids)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D22.part1()} (${elapsedTime(time)}ms)") // 587097
    time = System.nanoTime()
    println("Part 2: ${Y2021D22.part2()} (${elapsedTime(time)}ms)") // 1359673068597669
}