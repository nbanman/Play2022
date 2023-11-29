package org.gristle.adventOfCode.y2018.d25

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.MCoord
import org.gristle.adventOfCode.utilities.getInts

class Y2018D25(private val input: String) : Day {
    private fun MCoord.inRange(other: MCoord) = manhattanDistance(other) <= 3

    override fun part1(): Int {
        val points = input
            .getInts()
            .chunked(4) { MCoord(it) }

        val constellations = mutableListOf<List<MCoord>>()

        for (point in points) {
            val inRange = constellations
                .filter { constellation -> constellation.any { it.inRange(point) } }
            if (inRange.isEmpty()) {
                constellations.add(listOf(point))
            } else {
                val stars = inRange.sumOf { it.size } + 1
                val newConstellation = ArrayList<MCoord>(stars).apply {
                    add(point)
                    inRange.forEach { addAll(it) }
                }
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