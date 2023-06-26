package org.gristle.adventOfCode.y2018.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList

class Y2018D1(input: String) : Day {

    private val changes = input.getIntList()

    override fun part1() = changes.sum()

    override fun part2(): Int {
        val record = mutableSetOf<Int>()
        return generateSequence(0) { (it + 1) % changes.size }
            .map { changes[it] }
            .runningReduce(Int::plus)
            .first { !record.add(it) }
    }
}

fun main() = Day.runDay(Y2018D1::class)

//    Class creation: 22ms
//    Part 1: 433 (0ms)
//    Part 2: 256 (24ms)
//    Total time: 46ms