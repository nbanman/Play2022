package org.gristle.adventOfCode.y2015.d13

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.getPermutations
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

object Y2015D13 {
    data class Arrangement(val name: String, val neighbor: String, val happinessUnits: Int)

    private val input = readRawInput("y2015/d13")

    private const val pattern = """(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+)."""

    private val rules = (input
        .groupValues(pattern)
        .map { gv ->
            val happinessUnits = gv[2].toInt().let { if (gv[1] == "gain" ) it else -it }
            Arrangement(gv[0], gv[3], happinessUnits)
        })

    private val people = rules.map { it.name }.distinct()

    fun solve(folx: List<String>): Int {
        val permutations = folx
            .drop(1)
            .getPermutations()
            .map { it + folx.first() }

        val happiness = permutations
            .map { peopleList ->
                val pairs = peopleList.windowed(2) + listOf(listOf(peopleList.last(), peopleList.first()))
                pairs
                    .map { pair ->
                        val leftRight = rules
                            .find { rule -> pair.first() == rule.name && pair.last() == rule.neighbor }
                            ?.happinessUnits
                            ?: 0
                        val rightLeft = rules
                            .find { rule -> pair.last() == rule.name && pair.first() == rule.neighbor }
                            ?.happinessUnits
                            ?: 0
                        leftRight + rightLeft
                    }
            }
        return happiness.maxOf { it.sum() }
    }

    fun part1() = solve(people)

    fun part2() = solve(people + "me")
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D13.part1()} (${elapsedTime(time)}ms)") // 664
    time = System.nanoTime()
    println("Part 2: ${Y2015D13.part2()} (${elapsedTime(time)}ms)") // 640
}