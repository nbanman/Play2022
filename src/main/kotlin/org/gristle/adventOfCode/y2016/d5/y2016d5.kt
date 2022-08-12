package org.gristle.adventOfCode.y2016.d5

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.utilities.Md5.toHex

object Y2016D5 {
    private val input = readRawInput("y2016/d5")

    val md5Sequence = generateSequence(0) { it + 1 }
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
        md5Sequence
            .map { it[5] to it[6] }
            .forEach { charCharPair ->
                val place = charCharPair.first.toDigit()
                if (place in 0..7 && password[place] == '.') {
                    password[place] = charCharPair.second
                    if (password.all { it != '.' }) return password.joinToString("")
                }
            }
        return "Should never see this"
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D5.part1()} (${elapsedTime(time)}ms)") // 4543c154
    time = System.nanoTime()
    println("Part 2: ${Y2016D5.part2()} (${elapsedTime(time)}ms)") // 1050cbbd
}