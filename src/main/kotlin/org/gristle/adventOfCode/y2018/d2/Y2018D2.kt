package org.gristle.adventOfCode.y2018.d2

import org.gristle.adventOfCode.utilities.*

class Y2018D2(input: String) {
    private val boxIds = input.lines()
    fun part1(): Int {
        val frequencies = boxIds.map { it.eachCount().values }
        return frequencies.count { it.contains(2) } * frequencies.count { it.contains(3) }
    }

    fun part2(): String {
        fun List<String>.countDifferences() = first()
            .zip(last())
            .count { (first, last) -> first != last }

        fun List<String>.shared() = first()
            .zip(last())
            .filter { (first, last) -> first == last }
            .map { (first, _) -> first }
            .joinToString("")

        return boxIds.getPairs().first { it.countDifferences() == 1 }.shared()
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D2(readRawInput("y2018/d2"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 7688
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // lsrivmotzbdxpkxnaqmuwcchj
}