package org.gristle.adventOfCode.y2015.d24

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D24(input: String) {

    private val lines = input.lines()

    private fun List<Int>.qE() = this.fold(1L) { acc, i -> acc * i }

    private fun uniqueCombos(validCombos: List<List<Int>>, taken: List<List<Int>>, uniqueGroups: Int): List<Int>? {
        if (validCombos.isEmpty()) {
            return null
        } else {
            for (i in validCombos.indices) {
                if (validCombos[i].intersect(taken.flatten().toSet()).isEmpty()) {
                    return if (uniqueGroups > 3) {
                        val subUnique = uniqueCombos(
                            validCombos.drop(i),
                            taken + listOf(validCombos[i]),
                            uniqueGroups - 1
                        )
                        if (subUnique == null) continue else taken.first()
                    } else {
                        taken.first()
                    }
                }
            }
            return null
        }
    }

    private val weights = lines
        .map { it.toInt() }
        .sortedDescending()

    data class Package(val weight: Int, val range: IntRange)

    fun solve(numberOfGroups: Int): Long {
        val groupWeight = weights.sum() / numberOfGroups

        val packages = weights.mapIndexed { i, weight ->
            val lowEnd = groupWeight - weights.drop(i).sum()
            val highEnd = groupWeight - weight
            Package(weight, lowEnd..highEnd)
        }

        var combos = packages
            .filter { 0 in it.range }
            .map { listOf(it.weight) }

        var validCombos = mutableListOf<List<Int>>().apply {
            addAll(combos.filter { it.sum() == groupWeight })
        }

        // do a pass
        while (combos.isNotEmpty()) {
            val newCombos = mutableListOf<List<Int>>()

            val startIndex = packages.indexOf(packages.find { it.weight == combos.first().last() }) + 1

            var i = startIndex
            loop@while (i < packages.size) {
                for (combo in combos) {
                    if (combo.last() <= packages[i].weight) {
                        i++
                        continue@loop
                    }
                    if (combo.sum() in packages[i].range) {
                        val latestCombo = combo + packages[i].weight
                        if (latestCombo.sum() == groupWeight) {
                            validCombos.add(latestCombo)
                        } else {
                            newCombos.add(latestCombo)
                        }
                    }
                }
                i++
            }
            combos = newCombos
            if (validCombos.size >= numberOfGroups - 1) {
                validCombos = validCombos.sortedBy { it.qE() }.toMutableList()
                val unique = uniqueCombos(validCombos.drop(1), listOf(validCombos.first()), 3)
                if (unique != null) {
                    return unique.qE()
                }
            }
        }
        return -1L
    }


    fun part1() = solve(3)

    fun part2() = solve(4)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D24(readRawInput("y2015/d24"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 11846773891
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 80393059
}