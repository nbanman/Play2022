package org.gristle.adventOfCode.y2020.d6

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readStrippedInput

object Y2020D6 {
    // Read input and split into separate groups.
    private val groups = readStrippedInput("y2020/d6")
        .split("\n\n")

    // Both parts involve looking at each group separately, counting the answers in a particular way, then 
    // returning the sum of those counts. The "count" function takes a String representing a group and returns
    // a Set<Char> with each Char in the Set representing a "yes" answer counted in the manner specified by the
    // problem.
    fun solve(count: (String) -> Set<Char>) = groups
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
    println("Part 1: ${Y2020D6.part1()} (${elapsedTime(time)}ms)") // 6297
    time = System.nanoTime()
    println("Part 2: ${Y2020D6.part2()} (${elapsedTime(time)}ms)") // 3158
}