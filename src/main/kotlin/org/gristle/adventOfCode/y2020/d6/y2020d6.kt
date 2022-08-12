package org.gristle.adventOfCode.y2020.d6

import org.gristle.adventOfCode.utilities.*

object Y2020D6 {
    private val input = readStrippedInput("y2020/d6")

    private val groups = input
        .split("\n\n")

    fun part1() = groups.map { it.toSet() - '\n' }.sumOf { it.size }

    fun part2() = groups
        .map { group ->
            val people = group.split('\n').map { it.toSet() }
            people.drop(1).fold(people.first()) { acc, set ->
                acc.intersect(set)
            }
        }.sumOf { it.size }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D6.part1()} (${elapsedTime(time)}ms)") // 6297
    time = System.nanoTime()
    println("Part 2: ${Y2020D6.part2()} (${elapsedTime(time)}ms)") // 3158
}