package org.gristle.adventOfCode.y2021.d10

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import java.util.*

class Y2021D10(input: String) {
    private val lines = input.lines()

    // Functions and definitions to use with the "parse" function below.
    private fun Char.toCharScore1(): Long = when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException()
    }

    private fun Char.toCharScore2() = when (this) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> throw IllegalArgumentException()
    }

    private fun Iterable<Char>.toScore() = fold(0L) { acc, c -> acc * 5 + c.toCharScore2() }

    private val counterparts = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

    private inline fun String.parse(
        corruptReturn: (Char) -> Long?,
        incompleteReturn: (Deque<Char>) -> Long?
    ): Long? {
        val stack: Deque<Char> = ArrayDeque()
        for (index in indices) {
            val candidate = get(index)
            if (candidate !in counterparts.values) {
                stack.push(counterparts[candidate])
            } else {
                if (stack.isEmpty() || candidate != stack.pop()) return corruptReturn(candidate)
            }
        }
        return incompleteReturn(stack)
    }

    fun part1() = lines
        .sumOf { line ->
            line.parse(
                corruptReturn = { it.toCharScore1() },
                incompleteReturn = { null }
            ) ?: 0
        }

    fun part2() = lines
        .mapNotNull { line ->
            line.parse(
                corruptReturn = { null },
                incompleteReturn = { it.toScore() }
            )
        }.sorted()
        .let { it[it.size / 2] }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D10(readRawInput("y2021/d10"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 167379
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2776842859
}