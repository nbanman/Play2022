package org.gristle.adventOfCode.y2018.d2

import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2018D2(input: String) {
    private val boxIds = input.lines()

    private fun String.has(n: Int) = eachCount().values.any { it == n }

    fun part1() = boxIds.count { it.has(2) } * boxIds.count { it.has(3) }

    fun part2(): String {
        fun List<String>.countDifferences() = first()
            .zip(last())
            .count { (first, last) -> first != last }

        fun List<String>.shared() = first()
            .zip(last())
            .filter { (first, last) -> first == last }
            .map { (first, _) -> first }
            .joinToString("")

        fun <E> List<E>.getPairs(): List<List<E>> {
            val combos = mutableListOf<List<E>>()
            for (i in 0 until lastIndex) for (j in i + 1..lastIndex) combos.add(listOf(this[i], this[j]))
            return combos
        }

        return boxIds.getPairs().first { it.countDifferences() == 1 }.shared()
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D2(readRawInput("y2018/d2"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 7688
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // lsrivmotzbdxpkxnaqmuwcchj
}