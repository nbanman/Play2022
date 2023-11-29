package org.gristle.adventOfCode.y2022.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.containsAll
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.overlaps

class Y2022D4(input: String) : Day {

    private val ranges = input
        .getInts(true)
        .chunked(4) { (low1, high1, low2, high2) -> low1..high1 to low2..high2 }

    override fun part1() = ranges.count { (left, right) ->
        left.containsAll(right) || right.containsAll(left)
    }

    override fun part2() = ranges.count { (left, right) ->
        left.overlaps(right)
    }
}

fun main() = Day.runDay(Y2022D4::class)

//    Class creation: 3ms
//    Part 1: 605 (12ms)
//    Part 2: 914 (3ms)
//    Total time: 19ms