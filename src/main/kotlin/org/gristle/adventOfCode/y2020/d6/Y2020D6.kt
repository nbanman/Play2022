package org.gristle.adventOfCode.y2020.d6

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.*

class Y2020D6(input: String) : Day {
    // Read input and split into separate groups.
    private val groups = input
        .split("\n\n")

    // Both parts involve looking at each group separately, counting the answers in a particular way, then 
    // returning the sum of those counts. The "count" function takes a String representing a group and returns
    // a Set<Char> with each Char in the Set representing a "yes" answer counted in the manner specified by the
    // problem.
    private inline fun solve(count: (String) -> Set<Char>) = groups
        .map(count)
        .sumOf { it.size }

    // For each group, count the number of questions to which *anyone* answered "yes."
    // Simply converting to set removes duplicates, providing p1 answer
    override fun part1() = solve { group -> group.toSet() - '\n' }

    // For each group, count the number of questions to which *everyone* answered "yes."
    override fun part2() = solve { group ->
        group
            .split('\n') // split group into separate people
            .map(String::toSet) // represent each person as a set of characters
            .reduce(Set<Char>::intersect) // represent each group as set of characters shared by each person
    }
}

fun main() = Day.runDay(Y2020D6::class)

//    Class creation: 19ms
//    Part 1: 6297 (4ms)
//    Part 2: 3158 (4ms)
//    Total time: 29ms