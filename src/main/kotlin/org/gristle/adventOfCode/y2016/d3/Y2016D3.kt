package org.gristle.adventOfCode.y2016.d3

import org.gristle.adventOfCode.utilities.*

class Y2016D3(private val input: String) {
    data class Triangle(val a: Int, val b: Int, val c: Int) {
        private val asList = listOf(a, b, c).sorted()
        val isValid = asList[0] + asList[1] > asList[2]
    }

    fun part1(): Int {
        val pattern = """(\d+) +(\d+) +(\d+)"""
        return input
            .groupValues(pattern, String::toInt)
            .map { Triangle(it[0], it[1], it[2]) }
            .count(Triangle::isValid)
    }

    fun part2() = input
        .lines()
        .map { line -> line.split(Regex(" +")).mapNotNull(String::toIntOrNull) }
        .transpose()
        .map { lengths -> lengths.chunked(3) { Triangle(it[0], it[1], it[2]) } }
        .flatten()
        .count(Triangle::isValid)
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2016D3(readRawInput("y2016/d3"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 1032
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 1838
    println("Total time: ${timer.elapsed()}ms")
}