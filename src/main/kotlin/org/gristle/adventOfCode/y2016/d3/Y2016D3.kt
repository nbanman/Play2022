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
            .groupValues(pattern) { it.toInt() }
            .map { Triangle(it[0], it[1], it[2]) }
            .count { it.isValid }
    }

    fun part2() = input
        .lines()
        .map { line -> line.split(Regex(""" +""")).mapNotNull { it.toIntOrNull() } }
        .transpose()
        .map { lengths -> lengths.chunked(3).map { Triangle(it[0], it[1], it[2]) } }
        .flatten()
        .count { it.isValid }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D3(readRawInput("y2016/d3"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1032
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1838
}