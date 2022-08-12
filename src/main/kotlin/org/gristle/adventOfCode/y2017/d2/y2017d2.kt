package org.gristle.adventOfCode.y2017.d2

import org.gristle.adventOfCode.utilities.*

object Y2017D2 {
    private val input = readInput("y2017/d2")

    val spreadsheet = input.map { line -> line.split(' ', '\t').mapNotNull { it.toIntOrNull() } }

    fun part1() = spreadsheet
        .sumOf { (it.maxOrNull()?.minus(it.minOrNull() ?: 0) ?: 0) }

    fun part2() = spreadsheet.sumOf { row ->
        row.getPairs().sumBy { combo ->
            val (lesser, greater) = if (combo[0] < combo[1]) combo[0] to combo[1] else combo[1] to combo[0]
            if (greater % lesser == 0) greater / lesser else 0
        }
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D2.part1()} (${elapsedTime(time)}ms)") // 45972
    time = System.nanoTime()
    println("Part 2: ${Y2017D2.part2()} (${elapsedTime(time)}ms)") // 326
}