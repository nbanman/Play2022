package org.gristle.adventOfCode.y2017.d13

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts

class Y2017D13(input: String) : Day {

    private data class Layer(val depth: Int, val range: Int) {
        fun severity(): Int {
            return if ((depth) % ((range - 1) * 2) == 0) {
                depth * range
            } else 0
        }

        fun isTriggered(offset: Int) = (depth + offset) % ((range - 1) * 2) == 0
    }

    private val layers = input
        .getInts()
        .chunked(2)
        .map { Layer(it[0], it[1]) }
        .toList()

    override fun part1() = layers.sumOf(Layer::severity)

    override fun part2() = generateSequence(0) { it + 1 }
        .first { index -> layers.none { it.isTriggered(index) } }
}

fun main() = Day.runDay(Y2017D13::class)

//    Class creation: 13ms
//    Part 1: 1528 (0ms)
//    Part 2: 3896406 (149ms)
//    Total time: 162ms