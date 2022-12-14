package org.gristle.adventOfCode.y2016.d16

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

// Not refactored, but not terrible!

class Y2016D16(private val input: String) {

    private tailrec fun checkSum(a: String): String {
        val sum = a
            .chunked(2)
            .map { if (it == "00" || it == "11") '1' else '0' }
            .joinToString("")
        return if (sum.length % 2 == 1) {
            sum
        } else {
            checkSum(sum)
        }
    }

    private tailrec fun dragonCurve(a: String, diskSize: Int): String {
        return if (a.length >= diskSize) {
            a.substring(0, diskSize)
        } else {
            val b = a
                .reversed()
                .map { if(it == '1') '0' else '1' }
                .joinToString("")
            dragonCurve(a + '0' + b, diskSize)
        }

    }

    fun solve(diskSize: Int) = checkSum(dragonCurve(input, diskSize))

    fun part1() = solve(272)

    fun part2() = solve(35651584)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D16(readRawInput("y2016/d16"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 10010101010011101
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 01100111101101111
}
