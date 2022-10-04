package org.gristle.adventOfCode.y2018.d25

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.abs

class Y2018D25(private val input: String) {
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

        val constellations = mutableListOf<MutableList<SpaceTime>>()

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
    var time = System.nanoTime()
    val c = Y2018D25(readRawInput("y2018/d25"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 394
}