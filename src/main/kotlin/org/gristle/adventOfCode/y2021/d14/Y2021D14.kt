package org.gristle.adventOfCode.y2021.d14

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.stripCarriageReturn

class Y2021D14(input: String) {
    private val data = input.stripCarriageReturn()

    data class InsertionRule(val a: Char, val b: Char, val c: Char) {

        companion object {
            val propagate = mutableMapOf<String, Pair<String, String>>()
        }

        init {
            propagate["$a$b"] = "$a$c" to "$c$b"
        }
    }

    private val template = data.takeWhile { it != '\n' }

    private val rules = data
        .groupValues("""([A-Z])([A-Z]) -> ([A-Z])""")
        .map { InsertionRule(it[0][0], it[1][0], it[2][0]) }

    fun Map<String, Long>.step(): Map<String, Long> {
        val newPairs = mutableMapOf<String, Long>()
        forEach { (polyPair, amt) ->
            val (a, b) = InsertionRule.propagate[polyPair]!!
            newPairs[a] = (newPairs[a] ?: 0L) + amt
            newPairs[b] = (newPairs[b] ?: 0L) + amt
        }
        return newPairs
    }

    fun solve(steps: Int): Long {
        val proteins = rules.flatMap { listOf(it.a, it.b, it.c) }.toSet()
        val proteinPairs = mutableMapOf<String, Long>().apply {
            template.windowed(2).forEach { key ->
                this[key] = this[key]?.let { it + 1L } ?: 1L
            }
        }.toMap()
        val steppedPairs = (1..steps).fold(proteinPairs) { acc, _ -> acc.step() }
        val proteinCount = proteins.map { protein ->
            val count = steppedPairs
                .filter { it.key.contains(protein) }
                .map { if (it.key[0] == it.key[1]) it.value * 2 else it.value }
                .sum()
            val modifier = listOf(template.first(), template.last()).count { it == protein }
            (count + modifier) / 2
        }
        return proteinCount.maxOf { it } - proteinCount.minOf { it }
    }

    fun part1() = solve(10)

    fun part2() = solve(40)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D14(readRawInput("y2021/d14"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 3555
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 4439442043739
}