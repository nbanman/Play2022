package org.gristle.adventOfCode.y2017.d10

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.shift
import org.gristle.adventOfCode.y2017.shared.denseHash
import org.gristle.adventOfCode.y2017.shared.knotHash

class Y2017D10(private val input: String) : Day {

    override fun part1(): Int {
        val lengths = input.split(',').map { it.toInt() }
        val ring = List(256) { it }
        return ring.knotHash(lengths)
            .shift(0 - (lengths.sum() + ((1 until lengths.size).reduce { acc, i -> acc + i })))
            .take(2)
            .run {
                first() * last()
            }
    }

    override fun part2() = denseHash(input.map { it.code } + listOf(17, 31, 73, 47, 23))
}

fun main() = Day.runDay(Y2017D10::class)

//    Class creation: 11ms
//    Part 1: 23874 (1ms)
//    Part 2: e1a65bfb5a5ce396025fab5528c25a87 (29ms)
//    Total time: 42ms