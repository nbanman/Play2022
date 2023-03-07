package org.gristle.adventOfCode.y2022.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.containsAll
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.overlaps

class Y2022D4(input: String) : Day {

    private val ranges = input
        .getInts(true)
        .chunked(4)
        .map { it[0]..it[1] to it[2]..it[3] }

    override fun part1() = ranges.count { (left, right) ->
        left.containsAll(right) || right.containsAll(left)
    }

    override fun part2() = ranges.count { (left, right) ->
        left.overlaps(right)
    }
}

fun main() = Day.runDay(Y2022D4::class) // 605, 914