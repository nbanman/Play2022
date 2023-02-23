package org.gristle.adventOfCode.y2016.d19

import org.gristle.adventOfCode.Day
import kotlin.math.log10
import kotlin.math.pow

class Y2016D19(input: String) : Day {
    private val elves = input.toInt()

    override fun part1(): Int {
        val exponent = (log10(elves.toDouble()) / log10(2.0)).toInt()
        return (elves - (2.0.pow(exponent).toInt())) * 2 + 1
    }

    override fun part2(): Int {
        val exponent = (log10(elves.toDouble()) / log10(3.0)).toInt()
        val lastUp = 3.0.pow(exponent).toInt()
        val diff = elves - 3.0.pow(exponent).toInt()
        val ones = minOf(diff, lastUp)
        val twos = maxOf(diff - ones, 0)
        return ones + twos * 2
    }
}

// Class creation: 5ms
// Part 1: 1816277 (0ms)
// Part 2: 1410967 (0ms)
fun main() = Day.runDay(19, 2016, Y2016D19::class) 