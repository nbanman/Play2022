package org.gristle.adventOfCode.y2016.d5

import org.gristle.adventOfCode.utilities.Md5
import org.gristle.adventOfCode.utilities.Md5.toHex
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toDigit

class Y2016D5(input: String) {

    private val md5Sequence = generateSequence(0) { it + 1 }
        .map { Md5.getDigest(input + it) }
        .filter { it[0] == 0.toByte() && it[1] == 0.toByte() }
        .map { it.toHex() }
        .filter { it[4] == '0' }

    fun part1() = md5Sequence
        .map { it[5] }
        .take(8)
        .joinToString("")

    fun part2(): String {
        val password = CharArray(8) { '.' }
        return md5Sequence
            .mapNotNull { hash ->
                val place = hash[5].toDigit()
                if (place in 0..7 && password[place] == '.') {
                    password[place] = hash[6]
                    password
                } else {
                    null
                }
            }.first { password.all { it != '.' } }
            .joinToString("")
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D5(readRawInput("y2016/d5"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 4543c154
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1050cbbd
}