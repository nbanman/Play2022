package org.gristle.adventOfCode.y2017.d5

import org.gristle.adventOfCode.Day

class Y2017D5(input: String) : Day {

    val instructions = input.lines().map { it.toInt() }

    inline fun solve(incrementBehavior: (Int) -> Int): Int {
        val jumps = instructions.toMutableList()
        var pos = 0
        var counter = 0
        while (pos in jumps.indices) {
            counter++
            val inc = incrementBehavior(jumps[pos])
            jumps[pos] += inc
            pos += jumps[pos] - inc
        }
        return counter
    }

    override fun part1() = solve { 1 }

    override fun part2() = solve { jumps -> if (jumps >= 3) -1 else 1 }
}

fun main() = Day.runDay(5, 2017, Y2017D5::class)

//    Class creation: 22ms
//    Part 1: 373160 (29ms)
//    Part 2: 26395586 (275ms)
//    Total time: 327ms
