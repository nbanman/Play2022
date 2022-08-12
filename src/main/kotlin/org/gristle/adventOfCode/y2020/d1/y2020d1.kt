package org.gristle.adventOfCode.y2020.d1

import org.gristle.adventOfCode.utilities.*

object Y2020D1 {
    private val input = readInput("y2020/d1")

    private val entries = input.map { it.toInt() }
    private val entrySet = entries.toSet()
    private val pairEntries = entries.filter { it <= 1010 }

    fun part1(): Int {
        entrySet.forEach {
            if (entrySet.contains(2020 - it)) return it * (2020 - it)
        }
        return -1
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
        return -1
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D1.part1()} (${elapsedTime(time)}ms)") // 1015476
    time = System.nanoTime()
    println("Part 2: ${Y2020D1.part2()} (${elapsedTime(time)}ms)") // 200878544
}