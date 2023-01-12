package org.gristle.adventOfCode.y2022.d25

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import kotlin.math.ceil
import kotlin.math.log
import kotlin.math.pow

class Y2022D25(input: String) {

    private fun Char.toDigit() = when (this) {
        '=' -> -2
        '-' -> -1
        else -> this.digitToInt()
    }

    private fun List<Int>.toSnafu() = buildString {
        this@toSnafu.map {
            when (it) {
                -1 -> append('-')
                -2 -> append('=')
                else -> append(it)
            }
        }
    }.trimStart('0')

    private fun Long.toSnafu(): String {
        val maxPlaces = ceil(log(this.toDouble(), 5.0)).toInt()
        val maximums = (1 until maxPlaces).runningFold(2L) { acc, i ->
            acc + 2 * 5.0.pow(i).toLong()
        }.drop(0)
        val combos = (maxPlaces downTo 0).fold(listOf(emptyList<Int>() to this)) { acc, i ->
            val potentials = (-2..2).map { it * 5.0.pow(i).toLong() }
            acc.flatMap { (items, remaining) ->
                val combos = potentials.mapIndexedNotNull { index, potential ->
                    val newRemaining = remaining - potential
                    val limit = if (i == 0) 0L else maximums[i - 1]
                    if (newRemaining > limit || newRemaining < -limit) null else {
                        (items + (index - 2)) to newRemaining
                    }
                }
                combos
            }
        }
        return combos.find { (_, remaining) -> remaining == 0L }?.first?.toSnafu()
            ?: throw IllegalStateException("no combination found!")
    }

    private fun List<Int>.toInt() = foldIndexed(0L) { index, acc, i ->
        acc + (i * 5.0.pow(size - 1 - index)).toLong()
    }

    private val snafu = input.lines().map { line -> line.map { it.toDigit() } }

    fun part1() = snafu
        .sumOf { it.toInt() }
        .toSnafu()
}

fun main() {
    val input = getInput(25, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D25(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 2-=2-0=-0-=0200=--21 (solver)
    println("Total time: ${timer.elapsed()}ms")
}