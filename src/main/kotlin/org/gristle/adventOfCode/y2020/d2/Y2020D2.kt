package org.gristle.adventOfCode.y2020.d2

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D2(input: String) {

    data class PassPolicy(val letter: Char, val range: IntRange, val password: String) {
        val isValidUnderOldJobPolicy = password.count { it == letter } in range
        val isValidUnderCurrentPolicy =
            (password[range.first - 1] == letter) xor (password[range.last - 1] == letter)
    }

    private val policies = input
        .groupValues("""(\d+)-(\d+) ([a-z]): ([a-z]+)""")
        .map { gv ->
            val range = gv[0].toInt()..gv[1].toInt()
            PassPolicy(gv[2].first(), range, gv[3])
        }

    fun part1() = policies.count(PassPolicy::isValidUnderOldJobPolicy)
    fun part2() = policies.count(PassPolicy::isValidUnderCurrentPolicy)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D2(readRawInput("y2020/d2"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 445
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 491
}