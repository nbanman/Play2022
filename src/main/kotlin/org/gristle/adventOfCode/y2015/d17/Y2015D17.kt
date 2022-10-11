package org.gristle.adventOfCode.y2015.d17

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D17(input: String) {

    private val lines = input.lines()
    private fun getCombos(): List<List<Int>> {
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

    private val containers = lines.map(String::toInt).sortedDescending()

    private val storage = 150
    private val combos = getCombos()
    private val minimumContainers = combos.minOf(List<Int>::size)

    fun part1() = combos.size

    fun part2() = combos.filter { it.size == minimumContainers }.size
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D17(readRawInput("y2015/d17"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1638
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 17
}

