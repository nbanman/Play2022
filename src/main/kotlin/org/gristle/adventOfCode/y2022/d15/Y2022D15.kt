package org.gristle.adventOfCode.y2022.d15

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.minMaxBy
import kotlin.math.abs

class Y2022D15(input: String) : Day {

    data class Sensor(val pos: Coord, val beaconPos: Coord) {
        fun toRangeOrNull(y: Int): IntRange? {
            val xDistance = pos.manhattanDistance(beaconPos) - abs(pos.y - y)
            if (xDistance < 0) return null
            val absDist = abs(xDistance)
            return pos.x - absDist..pos.x + absDist
        }
    }

    private fun IntRange.contiguousOrNull(other: IntRange): IntRange? {
        val (lesser, greater) = minMaxBy(this, other) { it.first }
        return if (lesser.last >= greater.first) {
            lesser.first..kotlin.math.max(lesser.last, greater.last)
        } else {
            null
        }
    }

    private val sensors = input
        .getInts()
        .chunked(4)
        .map { Sensor(Coord(it[0], it[1]), Coord(it[2], it[3])) }
        .toList()

    private fun List<IntRange>.concatenate(): List<IntRange> {
        val mutableRanges = this.toMutableList()
        var i: Int
        var size = 0
        while (size != mutableRanges.size) {
            size = mutableRanges.size
            i = 0
            while (i < mutableRanges.lastIndex) {
                var j = i + 1
                while (j < mutableRanges.size) {
                    val union = mutableRanges[i].contiguousOrNull(mutableRanges[j])
                    if (union == null) j++ else {
                        mutableRanges[i] = union
                        mutableRanges.removeAt(j)
                    }
                }
                i++
            }
        }
        return mutableRanges
    }

    override fun part1() = sensors
        .mapNotNull { it.toRangeOrNull(2000000) }
        .concatenate()
        .sumOf { it.last - it.first }

    override fun part2() = generateSequence(0) { it + 1 }
        .map { y ->
            sensors
                .mapNotNull { it.toRangeOrNull(y) }
                .concatenate()
        }.withIndex()
        .first { (_, ranges) -> ranges.size > 1 }
        .let { (y, ranges) ->
            val x = ranges.minBy(IntRange::first).last + 1
            4_000_000L * x + y
        }
}

fun main() = Day.runDay(15, 2022, Y2022D15::class) // 5073496 (16ms), 13081194638237 (2411ms)