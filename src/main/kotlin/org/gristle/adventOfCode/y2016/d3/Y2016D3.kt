package org.gristle.adventOfCode.y2016.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.transpose

class Y2016D3(input: String) : Day {
    private val triangles = input.getInts().chunked(3).toList()
    private fun isValid(triangle: List<Int>) = triangle.sorted().let { (a, b, c) -> a + b > c }
    override fun part1() = triangles.count(::isValid)
    override fun part2() = triangles.transpose().flatMap { it.chunked(3) }.count(::isValid)
}

fun main() = Day.runDay(Y2016D3::class)

//    Class creation: 17ms
//    Part 1: 1032 (10ms)
//    Part 2: 1838 (5ms)
//    Total time: 33ms