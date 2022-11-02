package org.gristle.adventOfCode.y2021.d10

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import java.util.*

class Y2021D10(input: String) {
    private val lines = input.lines()

    private fun Char.toScore1(): Long = when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException()
    }

    private fun Char.toScore2() = when (this) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> throw IllegalArgumentException()
    }

    private val mapping = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )

    private val closures = setOf(')', ']', '}', '>')

    private fun parse(
        s: String,
        corruptReturn: (Char) -> Long?,
        incompleteReturn: (Deque<Char>) -> Long
    ): Long? {
        val stack: Deque<Char> = ArrayDeque()
        for (index in s.indices) {
            val candidate = s[index]
            if (candidate !in closures) {
                stack.push(mapping[candidate])
            } else {
                if (stack.isEmpty() || candidate != stack.pop()) return corruptReturn(candidate)
            }
        }
        return incompleteReturn(stack)
    }

    fun part1() = lines
        .sumOf { line ->
            parse(
                line,
                { c -> c.toScore1() },
                { 0 }
            ) ?: 0
        }

    fun part2() = lines
        .mapNotNull { line ->
            parse(
                line,
                { null },
                { stack -> stack.fold(0L) { acc, c -> acc * 5 + c.toScore2() } }
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