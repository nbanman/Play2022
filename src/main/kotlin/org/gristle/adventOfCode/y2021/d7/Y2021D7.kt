package org.gristle.adventOfCode.y2021.d7

import org.gristle.adventOfCode.Day
import kotlin.math.abs

class Y2021D7(input: String) : Day {

    private val crabs = input.split(',').map { it.toInt() }

    private val crabRange = crabs.minOf { it }..crabs.maxOf { it }

    private tailrec fun List<Int>.optimalAlignmentCost(range: IntRange = crabRange, fuelCost: (Int) -> Int): Int {

        fun List<Int>.alignmentCost(position: Int, fuelCost: (Int) -> Int) =
            sumOf { fuelCost(abs(it - position)) }

        fun IntRange.midPoint() = (last - first) / 2 + first

        if (range.first == range.last) return alignmentCost(range.first, fuelCost)
        val midPoint = range.midPoint()
        val newRange = listOf(
            range.first..midPoint,
            midPoint..range.last
        ).minBy { alignmentCost(it.midPoint(), fuelCost) }

        return optimalAlignmentCost(newRange, fuelCost)
    }

    override fun part1() = crabs.optimalAlignmentCost { it }

    override fun part2() = crabs.optimalAlignmentCost { (1..it).sum() }
}

fun main() = Day.runDay(7, 2021, Y2021D7::class)

//    Class creation: 7ms
//    Part 1: 343468 (6ms)
//    Part 2: 96086265 (17ms)
//    Total time: 31ms