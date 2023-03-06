package org.gristle.adventOfCode.y2018.d25

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.MCoord
import org.gristle.adventOfCode.utilities.groupValues

class Y2018D25(private val input: String) : Day {
    private val pattern = """(-?\d+),(-?\d+),(-?\d+),(-?\d+)""".toRegex()
    private fun MCoord.inRange(other: MCoord) = manhattanDistance(other) <= 3

    override fun part1(): Int {
        val points = input
            .groupValues(pattern) { it.toInt() }
            .map { MCoord(it[0], it[1], it[2], it[3]) }

        val constellations = mutableListOf<MutableList<MCoord>>()

        for (point in points) {
            val inRange = constellations
                .filter { constellation -> constellation.any { it.inRange(point) } }
                .toMutableList()
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

    override fun part2() = "Merry XMas!!!"
}

fun main() = Day.runDay(Y2018D25::class)

//    Class creation: 22ms
//    Part 1: 394 (173ms)
//    Total time: 195ms