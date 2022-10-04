package org.gristle.adventOfCode.y2020.d6

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.stripCarriageReturn

class Y2020D6(input: String) {
    // Read input and split into separate groups.
    private val groups = input
        .stripCarriageReturn()
        .split("\n\n")

    // Both parts involve looking at each group separately, counting the answers in a particular way, then 
    // returning the sum of those counts. The "count" function takes a String representing a group and returns
    // a Set<Char> with each Char in the Set representing a "yes" answer counted in the manner specified by the
    // problem.
    private inline fun solve(count: (String) -> Set<Char>) = groups
        .map(count)
        .sumOf { it.size }

    // For each group, count the number of questions to which *anyone* answered "yes."
    fun part1() = solve { group -> group.toSet() - '\n' } // Simply converting to set removes duplicates, providing p1 answer

    // For each group, count the number of questions to which *everyone* answered "yes."
    fun part2() = solve { group -> group
        .split('\n') // split group into separate people
        .map { it.toSet() } // represent each person as a set of characters
        .reduce { acc, set -> acc.intersect(set) } // represent each group as set of characters shared by each person
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D6(readRawInput("y2020/d6"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 6297
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 3158
}