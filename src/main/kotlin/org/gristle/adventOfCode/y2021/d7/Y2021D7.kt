package org.gristle.adventOfCode.y2021.d7

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.abs

class Y2021D7(input: String) {

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
        ).minByOrNull { alignmentCost(it.midPoint(), fuelCost) }!!

        return optimalAlignmentCost(newRange, fuelCost)
    }
    
    fun part1() = crabs.optimalAlignmentCost { it }

    fun part2() = crabs.optimalAlignmentCost { (1..it).sum() }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D7(readRawInput("y2021/d7"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 343468
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 96086265
}