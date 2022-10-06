package org.gristle.adventOfCode.y2020.d1

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D1(input: String) {

    private val entries = input.lines().map { it.toInt() }
    private val entrySet = entries.toSet()
    private val pairEntries = entries.filter { it <= 1010 }

    fun part1(): Int {
        entrySet.forEach {
            if (entrySet.contains(2020 - it)) return it * (2020 - it)
        }
        throw IllegalStateException("entrySet does not contain correct input")
    }

    fun part2(): Int {
        for (x in pairEntries.indices) {
            for (y in pairEntries.indices) {
                if (x == y) continue
                val third = 2020 - (pairEntries[x] + pairEntries[y])
                if (entrySet.contains(third))
                    return third * pairEntries[x] * pairEntries[y]
            }
        }
        throw IllegalStateException("entrySet does not contain correct input")
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D1(readRawInput("y2020/d1"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1015476
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 200878544
}