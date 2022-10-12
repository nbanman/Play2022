package org.gristle.adventOfCode.y2017.d2

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.getPairs
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D2(input: String) {

    private val spreadsheet = input
        .lines()
        .map { line ->
            line
                .split(' ', '\t')
                .mapNotNull(String::toIntOrNull)
        }

    fun part1() = spreadsheet
        .sumOf { (it.maxOrNull()?.minus(it.minOrNull() ?: 0) ?: 0) }

    fun part2() = spreadsheet.sumOf { row ->
        row.getPairs().sumOf { combo ->
            val (lesser, greater) = if (combo[0] < combo[1]) combo[0] to combo[1] else combo[1] to combo[0]
            if (greater % lesser == 0) greater / lesser else 0
        }
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D2(readRawInput("y2017/d2"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 45972
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 326
}