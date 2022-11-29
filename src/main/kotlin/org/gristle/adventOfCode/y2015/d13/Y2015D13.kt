package org.gristle.adventOfCode.y2015.d13

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getPermutations
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D13(input: String) {

    private val arrangements = buildMap<String, MutableMap<String, Int>> {
        val pattern = """(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+).""".toRegex()
        input
            .groupValues(pattern)
            .forEach { gv ->
                val happinessUnits = gv[2].toInt().let { if (gv[1] == "gain") it else -it }
                computeIfAbsent(gv[0]) { mutableMapOf() }[gv[3]] = happinessUnits
            }
    } as Map<String, Map<String, Int>>

    private val people = arrangements.keys

    fun solve(people: Set<String>): Int = people
        .drop(1) // remove first person because all permutations have to start somewhere
        .getPermutations() // get all permutations of remaining people
        .map { it + people.first() } // re-add the first person to all permutations
        .maxOf { peopleList -> // calculate the happiness units for each permutation and return the maximum
            val pairs = peopleList.zipWithNext() + (peopleList.last() to peopleList.first())
            pairs.sumOf { (left, right) ->
                val leftRight = arrangements[left]?.get(right) ?: 0
                val rightLeft = arrangements[right]?.get(left) ?: 0
                leftRight + rightLeft
            }
        }

    fun part1() = solve(people)

    fun part2() = solve(people + "me")
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2015D13(readRawInput("y2015/d13"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)")
    println("Part 2: ${c.part2()} (${timer.lap()}ms)")
}
