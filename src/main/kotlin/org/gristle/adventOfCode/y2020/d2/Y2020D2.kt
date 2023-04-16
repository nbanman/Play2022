package org.gristle.adventOfCode.y2020.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues

class Y2020D2(input: String) : Day {

    data class PassPolicy(val letter: Char, val range: IntRange, val password: String) {
        fun isValidUnderOldJobPolicy() = password.count { it == letter } in range
        fun isValidUnderCurrentPolicy() =
            (password[range.first - 1] == letter) xor (password[range.last - 1] == letter)
    }

    private val policies = input
        .groupValues("""(\d+)-(\d+) ([a-z]): ([a-z]+)""")
        .map { gv ->
            val range = gv[0].toInt()..gv[1].toInt()
            PassPolicy(gv[2].first(), range, gv[3])
        }

    override fun part1() = policies.count(PassPolicy::isValidUnderOldJobPolicy)
    override fun part2() = policies.count(PassPolicy::isValidUnderCurrentPolicy)
}

fun main() = Day.runDay(Y2020D2::class)

//    Class creation: 32ms
//    Part 1: 445 (0ms)
//    Part 2: 491 (0ms)
//    Total time: 33ms