package org.gristle.adventOfCode.y2020.d6

import org.gristle.adventOfCode.utilities.*

class Y2020D6(input: String) {
    // Read input and split into separate groups.
    private val groups = input
        .split("\n\n")

    // Both parts involve looking at each group separately, counting the answers in a particular way, then 
    // returning the sum of those counts. The "count" function takes a String representing a group and returns
    // a Set<Char> with each Char in the Set representing a "yes" answer counted in the manner specified by the
    // problem.
    private inline fun solve(count: (String) -> Set<Char>) = groups
        .map(count)
        .sumOf(Set<Char>::size)

    // For each group, count the number of questions to which *anyone* answered "yes."
    // Simply converting to set removes duplicates, providing p1 answer
    fun part1() = solve { group -> group.toSet() - '\n' }

    // For each group, count the number of questions to which *everyone* answered "yes."
    fun part2() = solve { group ->
        group
            .split('\n') // split group into separate people
            .map(String::toSet) // represent each person as a set of characters
            .reduce(Set<Char>::intersect) // represent each group as set of characters shared by each person
    }
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2020D6(readStrippedInput("y2020/d6"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 6297
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 3158
    println("Total time: ${timer.elapsed()}ms")
}