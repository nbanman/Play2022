package org.gristle.adventOfCode.y2017.d15

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

// Not refactored style-wise. Sped up 8x by getting rid of string conversion and doing a bitwise strip.
class Y2017D15(input: String) {
    private val pattern = Regex("""(\d+)""")
    private val generatorValues = pattern.findAll(input).map { it.value.toLong() }.toList()

    data class Generator (val seed: Long, val factor: Int, val multiples: Int = 1) {
        val div = 2147483647
        var value = seed
        private fun newValue() {
            value = (value * factor) % div
        }

        fun next(): Long {
            do {
                newValue()
            } while (value % multiples != 0L)
            return value and 65535L
        }
    }

    fun part1(): Int {
        val genA = Generator(generatorValues[0], 16807)
        val genB = Generator(generatorValues[1], 48271)
        return (1..40_000_000).count { genA.next() == genB.next() }
    }

    fun part2(): Int {
        val genA = Generator(generatorValues[0], 16807, 4)
        val genB = Generator(generatorValues[1], 48271, 8)
        return (1..5_000_000).count { genA.next() == genB.next() }
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D15(readRawInput("y2017/d15"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 594
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 328
}