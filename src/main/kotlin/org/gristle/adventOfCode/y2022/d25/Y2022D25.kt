package org.gristle.adventOfCode.y2022.d25

import org.gristle.adventOfCode.Day

private typealias Snafu = String

class Y2022D25(private val input: String) : Day {

    private fun Long.toSnafu(): Snafu = generateSequence(this) { (it + 2) / 5 }
        .takeWhile { it != 0L }
        .map { "012-="[(it % 5).toInt()] }
        .joinToString("")
        .reversed()

    private fun Snafu.toBase10() = fold(0L) { acc, c ->
        acc * 5 + when (c) {
            '-' -> -1
            '=' -> -2
            else -> c.digitToInt()
        }
    }

    override fun part1() = input
        .lineSequence()
        .sumOf { it.toBase10() }
        .toSnafu()

    override fun part2() = "Damned interfaces!"
}

fun main() = Day.runDay(Y2022D25::class) // 2-=2-0=-0-=0200=--21 (29ms)