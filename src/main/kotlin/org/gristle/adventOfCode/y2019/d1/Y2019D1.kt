package org.gristle.adventOfCode.y2019.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import kotlin.math.max

class Y2019D1(input: String) : Day {
    private val modules = input.getIntList()
    private fun baseFuel(weight: Int): Int = weight / 3 - 2
    private tailrec fun totalFuel(remaining: Int, total: Int = 0): Int {
        return if (remaining == 0) {
            total
        } else {
            val fuel = max(0, baseFuel(remaining))
            totalFuel(fuel, total + fuel)
        }
    }
    override fun part1() = modules.sumOf(::baseFuel)
    override fun part2() = modules.sumOf(::totalFuel)
}

fun main() = Day.runDay(Y2019D1::class)

//    Class creation: 2ms
//    Part 1: 3325347 (0ms)
//    Part 2: 4985145 (0ms)
//    Total time: 2ms