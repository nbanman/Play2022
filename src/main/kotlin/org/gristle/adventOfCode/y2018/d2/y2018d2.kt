package org.gristle.adventOfCode.y2018.d2

import org.gristle.adventOfCode.utilities.*

object Y2018D2 {
    private val boxIds = readInput("y2018/d2")

    private fun String.containsPair() = groupingBy { it }.eachCount().values.any { it == 2 }

    private fun String.containsTrips() = groupingBy { it }.eachCount().values.any { it == 3 }

    fun part1() = boxIds.count { it.containsPair() } * boxIds.count { it.containsTrips() }

    fun part2(): String {
        fun List<String>.countDifferences(): Int {
            val second = last()
            return first().foldIndexed(0) { index, acc, c -> acc + if (c == second[index]) 0 else 1 }
        }

        fun List<String>.shared(): String {
            val second = last()
            return first().foldIndexed("") { index, acc, c -> acc + if (c == second[index]) c.toString() else "" }
        }

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
    println("Part 1: ${Y2018D2.part1()} (${elapsedTime(time)}ms)") // 7688
    time = System.nanoTime()
    println("Part 2: ${Y2018D2.part2()} (${elapsedTime(time)}ms)") // lsrivmotzbdxpkxnaqmuwcchj 
}