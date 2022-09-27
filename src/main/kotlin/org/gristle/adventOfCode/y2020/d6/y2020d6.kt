package org.gristle.adventOfCode.y2020.d6

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readStrippedInput

object Y2020D6 {
    // Read input and split into separate groups.
    private val groups = readStrippedInput("y2020/d6")
        .split("\n\n")

    // For each group, count the number of questions to which anyone answered "yes;" return the sum of those counts.
    fun part1() = groups
        .map { it.toSet() - '\n' } // converting string to set strips duplicates, then remove newline
        .sumOf { it.size } // sum of the size of the sets

    // For each group, count the number of questions to which everyone answered "yes;" return the sum of those counts
    fun part2() = groups
        .map { group ->
            group // deal with each group separately
                .split('\n') // split group into separate people
                .map { it.toSet() } // represent each person as a set of characters
                .reduce { acc, set -> acc.intersect(set) } // represent each group as set of characters shared by each person
        }.sumOf { it.size } // sum of the size of each count
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D6.part1()} (${elapsedTime(time)}ms)") // 6297
    time = System.nanoTime()
    println("Part 2: ${Y2020D6.part2()} (${elapsedTime(time)}ms)") // 3158
}