package org.gristle.adventOfCode.y2018.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList

class Y2018D1(input: String) : Day {

    private val changes = input.getIntList()

    private tailrec fun calibrate(
        freq: Int = 0,
        states: MutableSet<Int> = mutableSetOf(),
        index: Int = 0,
    ): Int {
        val newState = freq + changes[index]
        return if (newState in states) {
            newState
        } else {
            states.add(newState)
            calibrate(newState, states, (index + 1) % changes.size)
        }
    }

    override fun part1() = changes.sum()

    override fun part2() = calibrate()
}

fun main() = Day.runDay(Y2018D1::class)

//    Class creation: 22ms
//    Part 1: 433 (0ms)
//    Part 2: 256 (24ms)
//    Total time: 46ms