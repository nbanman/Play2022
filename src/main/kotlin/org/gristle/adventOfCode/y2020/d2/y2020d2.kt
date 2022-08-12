package org.gristle.adventOfCode.y2020.d2

import org.gristle.adventOfCode.utilities.*

object Y2020D2 {
    private val input = readRawInput("y2020/d2")

    data class PassPolicy(val letter: Char, val range: IntRange, val password: String) {
        val isValidUnderOldJobPolicy = password.count { it == letter } in range
        val isValidUnderCurrentPolicy =
            1 == ((if (password[range.first - 1] == letter) 1 else 0) +
                    (if (password[range.last - 1] == letter) 1 else 0))
    }

    val policies = input
        .groupValues("""(\d+)-(\d+) ([a-z]): ([a-z]+)""")
        .map { gv ->
            val range = gv[0].toInt()..gv[1].toInt()
            PassPolicy(gv[2].first(), range, gv[3])
        }

    fun part1() = policies.count { it.isValidUnderOldJobPolicy }
    fun part2() = policies.count { it.isValidUnderCurrentPolicy }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D2.part1()} (${elapsedTime(time)}ms)") // 445
    time = System.nanoTime()
    println("Part 2: ${Y2020D2.part2()} (${elapsedTime(time)}ms)") // 491
}