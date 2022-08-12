package org.gristle.adventOfCode.y2021.d6

import org.gristle.adventOfCode.utilities.*

object Y2021D6 {
    private val input = readRawInput("y2021/d6")

    private val fish = input
        .split(',')
        .map { it.toInt() }
        .groupBy { it }

    // initialize count of fish by day
    private val fishByDays = List(9) { i ->
        fish[i]?.size?.toLong() ?: 0L
    }

    fun solve(days: Int): Long {

        fun List<Long>.propagate() = List(9) { i ->
            when (i) {
                6 -> get(0) + get(7)
                8 -> get(0)
                else -> get(i + 1)
            }
        }

        return (1..days)
            .fold(fishByDays) { acc, _ -> acc.propagate() }
            .sum()
    }
    
    fun part1() = solve(80)

    fun part2() = solve(256)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D6.part1()} (${elapsedTime(time)}ms)") // 361169
    time = System.nanoTime()
    println("Part 2: ${Y2021D6.part2()} (${elapsedTime(time)}ms)") // 1634946868992
}