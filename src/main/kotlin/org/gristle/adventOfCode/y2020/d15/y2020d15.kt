package org.gristle.adventOfCode.y2020.d15

import org.gristle.adventOfCode.utilities.*

object Y2020D15 {
    private val input = readRawInput("y2020/d15")

    val start = input.split(',').map { it.toInt() }

    data class Spokenses(val first: Int? = null, val last: Int? = null) {

        companion object {
            val DEFAULT = Spokenses()
        }

        fun update(n: Int): Spokenses {
            return when {
                first == null -> Spokenses(n, null)
                last == null -> Spokenses(first, n)
                else -> Spokenses(last, n)
            }
        }
    }

    fun lastNumberSpoken(iterations: Int): Int {

        val log = start
            .mapIndexed { index, i -> i to Spokenses(index + 1) }
            .toTypedArray()
            .let { mutableMapOf(*it) }

        var lastNumberSpoken = start.last()

        for (turn in (start.size + 1)..iterations) {
            val spoke = log[lastNumberSpoken]!!
            lastNumberSpoken = if (spoke.last == null) {
                0
            } else {
                spoke.last - spoke.first!!
            }
            log[lastNumberSpoken] = log.getOrDefault(lastNumberSpoken, Spokenses.DEFAULT).update(turn)
        }
        return lastNumberSpoken
    }

    fun part1() = lastNumberSpoken(2020)

    fun part2() = lastNumberSpoken(30_000_000)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D15.part1()} (${elapsedTime(time)}ms)") // 929
    time = System.nanoTime()
    println("Part 2: ${Y2020D15.part2()} (${elapsedTime(time)}ms)") // 16671510
}