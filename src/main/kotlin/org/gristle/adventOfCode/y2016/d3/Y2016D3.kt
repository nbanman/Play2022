package org.gristle.adventOfCode.y2016.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.transpose

private typealias Triangle = List<Int>

private fun Triangle.isValid(): Boolean {
    val (a, b, c) = this
    val max = maxOf(a, b, c)
    return a + b + c - max > max
}

class Y2016D3(input: String) : Day {

    private val triangles = input.getInts().chunked(3)

    override fun part1(): Int = triangles.count { it.isValid() }

    override fun part2(): Int = triangles
        .toList()
        .transpose()
        .flatMap { lengths -> lengths.chunked(3) }
        .count { it.isValid() }
}

fun main() = Day.runDay(3, 2016, Y2016D3::class) // 1032, 1838 