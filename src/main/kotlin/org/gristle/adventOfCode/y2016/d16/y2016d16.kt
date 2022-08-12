package org.gristle.adventOfCode.y2016.d16

import org.gristle.adventOfCode.utilities.*

// Not refactored, but not terrible!

object Y2016D16 {
    private val input = "10001110011110000"

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
    println("Part 1: ${Y2016D16.part1()} (${elapsedTime(time)}ms)") // 10010101010011101
    time = System.nanoTime()
    println("Part 2: ${Y2016D16.part2()} (${elapsedTime(time)}ms)") // 01100111101101111
}