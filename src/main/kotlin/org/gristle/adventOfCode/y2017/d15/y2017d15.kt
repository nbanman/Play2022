package org.gristle.adventOfCode.y2017.d15

import org.gristle.adventOfCode.utilities.*

// Not refactored style-wise. Sped up 8x by getting rid of string conversion and doing a bitwise strip.
object Y2017D15 {
    private val pattern = Regex("""(\d+)""")
    private val input = pattern.findAll(readRawInput("y2017/d15")).map { it.value.toLong() }.toList()

    data class Generator (val seed: Long, val factor: Int, val multiples: Int = 1) {
        val div = 2147483647
        var value = seed
        fun newValue() {
            value = (value * factor) % div
        }
        fun next(): Long {
            do {
                newValue()
            } while (value % multiples != 0L)
            return value and 65535L
        }

        fun nextFull(): Long {
            do {
                newValue()
            } while (value % multiples != 0L)
            return value
        }
    }

    fun part1(): Int {
        val genA = Generator(input[0], 16807)
        val genB = Generator(input[1], 48271)
        return (1..40_000_000).count { genA.next() == genB.next() }
    }

    fun part2(): Int {
        val genA = Generator(703, 16807, 4)
        val genB = Generator(516, 48271, 8)
        return (1..5_000_000).count { genA.next() == genB.next() }
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D15.part1()} (${elapsedTime(time)}ms)") // 594
    time = System.nanoTime()
    println("Part 2: ${Y2017D15.part2()} (${elapsedTime(time)}ms)") // 328
}