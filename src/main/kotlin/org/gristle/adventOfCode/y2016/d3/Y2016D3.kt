package org.gristle.adventOfCode.y2016.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.transpose

private typealias Triangle = List<Int>

class Y2016D3(input: String) : Day {

    private val triangles = input.getInts().chunked(3)

    private fun Triangle.isValid() = sum() - max() > max()

    override fun part1(): Int = triangles.count { it.isValid() }

    override fun part2(): Int = triangles
        .toList()
        .transpose()
        .flatMap { lengths -> lengths.chunked(3) }
        .count { it.isValid() }
}

fun main() = Day.runDay(Y2016D3::class)

//    Class creation: 17ms
//    Part 1: 1032 (10ms)
//    Part 2: 1838 (5ms)
//    Total time: 33ms