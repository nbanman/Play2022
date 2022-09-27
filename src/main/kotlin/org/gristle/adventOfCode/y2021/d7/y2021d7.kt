package org.gristle.adventOfCode.y2021.d7

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.abs

object Y2021D7 {
    private val input = readRawInput("y2021/d7")

    private val crabs = input.split(',').map { it.toInt() }

    private val crabRange = crabs.minOf { it }..crabs.maxOf { it }

    private tailrec fun List<Int>.optimalAlignmentCost(range: IntRange, fuelCost: (Int) -> Int): Int {

        fun List<Int>.alignmentCost(position: Int, fuelCost: (Int) -> Int) =
            sumOf { fuelCost(abs(it - position)) }

        fun IntRange.midPoint() = (last - first) / 2 + first

        if (range.first == range.last) return alignmentCost(range.first, fuelCost)
        val midPoint = range.midPoint()
        val newRange = listOf(
            range.first..midPoint,
            midPoint..range.last
        ).minByOrNull { alignmentCost(it.midPoint(), fuelCost) }!!

        return optimalAlignmentCost(newRange, fuelCost)
    }
    
    fun part1() = crabs.optimalAlignmentCost(crabRange) { it }

    fun part2() = crabs.optimalAlignmentCost(crabRange) { (1..it).sum() }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D7.part1()} (${elapsedTime(time)}ms)") // 343468
    time = System.nanoTime()
    println("Part 2: ${Y2021D7.part2()} (${elapsedTime(time)}ms)") // 96086265
}