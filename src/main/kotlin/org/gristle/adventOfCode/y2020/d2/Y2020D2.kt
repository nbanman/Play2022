package org.gristle.adventOfCode.y2020.d2

import org.gristle.adventOfCode.Day

class Y2020D2(input: String) : Day {

    data class PassPolicy(val letter: Char, val range: IntRange, val password: String) {
        fun isValidUnderOldJobPolicy() = password.count { it == letter } in range
        fun isValidUnderCurrentPolicy() =
            (password[range.first - 1] == letter) xor (password[range.last - 1] == letter)
    }

    private val policies = input
        .lineSequence()
        .map {
            val (lower, upper, letter, password) = it.split('-', ' ')
            PassPolicy(letter.first(), lower.toInt()..upper.toInt(), password)
        }.toList()

    override fun part1() = policies.count(PassPolicy::isValidUnderOldJobPolicy)
    override fun part2() = policies.count(PassPolicy::isValidUnderCurrentPolicy)
}

fun main() = Day.runDay(Y2020D2::class)

//    Class creation: 32ms
//    Part 1: 445 (0ms)
//    Part 2: 491 (0ms)
//    Total time: 33ms