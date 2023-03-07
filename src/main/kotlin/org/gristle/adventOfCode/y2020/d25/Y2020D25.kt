package org.gristle.adventOfCode.y2020.d25

import org.gristle.adventOfCode.Day

class Y2020D25(input: String) : Day {

    private val divisor = 20201227

    private val keys = input.lines().map(String::toLong)
    private val cardKey = keys[0]
    private val doorKey = keys[1]

    override fun part1(): Long {
        val loopSize = generateSequence(0 to 1L) { (count, value) -> (count + 1) to (value * 7) % divisor }
            .first { (_, value) -> value == cardKey }
            .first
        return generateSequence(doorKey % divisor) { (it * doorKey) % divisor }
            .take(loopSize)
            .last()
    }

    override fun part2() = "Merry XMas!!!"
}

fun main() = Day.runDay(Y2020D25::class)

//    Class creation: 18ms
//    Part 1: 296776 (50ms)
//    Total time: 68ms
