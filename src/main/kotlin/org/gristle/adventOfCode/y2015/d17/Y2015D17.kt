package org.gristle.adventOfCode.y2015.d17

import org.gristle.adventOfCode.Day

class Y2015D17(input: String) : Day {

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

    override fun part1() = combos.size

    override fun part2() = combos.filter { it.size == minimumContainers }.size
}

fun main() = Day.runDay(Y2015D17::class)

//    Class creation: 75ms
//    Part 1: 1638 (0ms)
//    Part 2: 17 (0ms)
//    Total time: 76ms