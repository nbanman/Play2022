package org.gristle.adventOfCode.y2019.d4

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2019D4(input: String) {

    private val range = input
        .split('-')
        .map(String::toInt)
        .let { it[0]..it[1] }

    private val part1Passwords = range
        .filter { password ->
            val passString = password.toString()
            val zippedPassString = passString.zipWithNext()
            zippedPassString.all { it.first <= it.second }
                    && zippedPassString.any { it.first == it.second }
        }.map(Int::toString)

    fun part1() = part1Passwords.size

    fun part2(): Int {
        val pattern = Regex("""(\d)\1+""")
        return part1Passwords
            .filter { password -> pattern.findAll(password).any { it.value.length == 2 } }
            .size
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D4(readRawInput("y2019/d4"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 466
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 292
}