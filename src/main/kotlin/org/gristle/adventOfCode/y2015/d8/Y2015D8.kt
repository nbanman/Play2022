package org.gristle.adventOfCode.y2015.d8

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D8(input: String) {
    private val lines = input.lines()

    private fun String.charsInMemory(): Int {
        return drop(1).dropLast(1).replace("""\\\\|\\"|\\x[\da-f]{2}""".toRegex(), "X").length
    }

    private fun String.encodedLength() = length + 2 + count { it in """\"""" }

    fun part1() = lines.sumOf { it.length } - lines.sumOf { it.charsInMemory() }

    fun part2() = lines.sumOf { it.encodedLength() } - lines.sumOf { it.length }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D8(readRawInput("y2015/d8"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1333
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2046
}