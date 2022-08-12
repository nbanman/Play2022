package org.gristle.adventOfCode.y2018.d25

import org.gristle.adventOfCode.utilities.*
import kotlin.math.abs

object Y2018D25 {
    private val input = readRawInput("y2018/d25")
    private val pattern = """(-?\d+),(-?\d+),(-?\d+),(-?\d+)""".toRegex()

    data class SpaceTime(val x: Int, val y: Int, val z: Int, val t: Int) {
        fun manhattanDistance(other: SpaceTime) =
            abs(x - other.x) + abs(y - other.y) + abs(z - other.z) + abs(t - other.t)
        fun inRange(other: SpaceTime) = manhattanDistance(other) <= 3
    }

    fun part1(): Int {
        val points = input
            .groupValues(pattern) { it.toInt() }
            .map { SpaceTime(it[0], it[1], it[2], it[3]) }

        var constellations = mutableListOf<MutableList<SpaceTime>>()

        for (point in points) {
            val inRange = constellations.filter { constellation -> constellation.any { it.inRange(point) } }.toMutableList()
            if (inRange.size == 0) {
                constellations.add(mutableListOf(point))
            } else {
                val newConstellation = inRange.flatten().toMutableList().apply { add(point) }
                constellations.removeAll(inRange)
                constellations.add(newConstellation)
            }
        }
        return constellations.size
    }
}

fun main() {
    val time = System.nanoTime()
    println("Part 1: ${Y2018D25.part1()} (${elapsedTime(time)}ms)") // 394 
}