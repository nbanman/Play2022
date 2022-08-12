package org.gristle.adventOfCode.y2015.d17

import org.gristle.adventOfCode.utilities.*

object Y2015D17 {
    private val input = readInput("y2015/d17")

    fun getCombos(containers: List<Int>, storage: Int): List<List<Int>> {
        tailrec fun gC(remaining: List<Int>, combos: List<List<Int>>): List<List<Int>> {
            return if (remaining.isNotEmpty()) {
                val latest = remaining.first()
                val newCombos = combos.fold(mutableListOf<List<Int>>()) { acc, combo ->
                    if (combo.sum() + latest <= storage) {
                        acc.addAll(listOf(combo) + listOf(combo + latest))
                    } else {
                        acc.addAll(listOf(combo))
                    }
                    acc
                } + listOf(listOf(latest))

                gC(remaining.drop(1), newCombos)
            } else {
                combos
            }
        }
        return gC(containers, emptyList()).filter { it.sum() == storage }
    }

    private val containers = input
        .map { it.toInt() }
        .sortedDescending()
    private const val storage = 150
    private val combos = getCombos(containers, storage)
    private val minimumContainers = combos.minByOrNull { it.size }!!.size

    fun part1() = combos.size

    fun part2() = combos.filter { it.size == minimumContainers }.size
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D17.part1()} (${elapsedTime(time)}ms)") // 1638
    time = System.nanoTime()
    println("Part 2: ${Y2015D17.part2()} (${elapsedTime(time)}ms)") // 17
}