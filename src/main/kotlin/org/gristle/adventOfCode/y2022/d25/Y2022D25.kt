package org.gristle.adventOfCode.y2022.d25

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D25(private val input: String) {

    private fun Long.toSnafu() = generateSequence(this) { (it + 2) / 5 }
        .takeWhile { it != 0L }
        .fold(StringBuilder()) { snafu, remaining ->
            snafu.append("012-="[(remaining % 5).toInt()])
        }.reverse().toString()

    private fun String.toBase10() = fold(0L) { acc, c ->
        acc * 5 + when (c) {
            '-' -> -1
            '=' -> -2
            else -> c.digitToInt()
        }
    }

    fun part1() = input
        .lines()
        .sumOf { it.toBase10() }
        .toSnafu()
}

fun main() {
    val input = getInput(25, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D25(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 2-=2-0=-0-=0200=--21 (4ms)
    println("Total time: ${timer.elapsed()}ms")
}