package org.gristle.adventOfCode.y2015.d8

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readInput

object Y2015D8 {
    private val lines = readInput("y2015/d8")

    private fun String.charsInMemory(): Int {
        return drop(1).dropLast(1).replace("""\\\\|\\"|\\x[\da-f]{2}""".toRegex(), "X").length
    }

    private fun String.encodedLength() = length + 2 + count { it in """\"""" }

    fun part1() = lines.sumOf { it.length } - lines.sumOf { it.charsInMemory() }

    fun part2() = lines.sumOf { it.encodedLength() } - lines.sumOf { it.length }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D8.part1()} (${elapsedTime(time)}ms)") // 1333
    time = System.nanoTime()
    println("Part 2: ${Y2015D8.part2()} (${elapsedTime(time)}ms)") // 2046
}