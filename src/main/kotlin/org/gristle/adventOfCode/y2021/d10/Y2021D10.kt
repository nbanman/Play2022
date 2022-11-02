package org.gristle.adventOfCode.y2021.d10

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import java.util.*

class Y2021D10(input: String) {
    private val lines = input.lines()

    // Functions and definitions to use with the "parse" function below.

    // pairs opening characters with corresponding closing characters
    private val counterparts = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

    // character scoring provided for part1
    private fun Char.syntaxErrorScore(): Long = when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException()
    }

    // character scoring provided for part2
    private fun Char.pointValue() = when (this) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> throw IllegalArgumentException()
    }

    // completion string scoring per part2
    private fun Iterable<Char>.toScore() = fold(0L) { acc, c -> acc * 5 + c.pointValue() }

    /**
     * Parses each character in a string. If it's an opening character, add the corresponding closing character
     * to a stack. If it's a closing character, pop from the stack and compare the two. If they are the same, they
     * cancel out and continue. If they are different, then the string is corrupt.
     *
     * The function accepts two functions as parameters that return a nullable Long. onCorrupt gets called early
     * if the string is corrupt. onFinish gets called after every character in the string has been parsed.
     */
    private inline fun String.parse(
        onCorrupt: (Char) -> Long?,
        onFinish: (Iterable<Char>) -> Long?
    ): Long? {
        val stack: Deque<Char> = ArrayDeque()
        forEach { candidate ->
            if (candidate !in counterparts.values) {
                stack.push(counterparts[candidate])
            } else {
                if (candidate != stack.pop()) return onCorrupt(candidate)
            }
        }
        return onFinish(stack)
    }

    // sums the syntax error scores
    fun part1() = lines
        .sumOf { line ->
            line.parse(
                onCorrupt = { it.syntaxErrorScore() },
                onFinish = { null }
            ) ?: 0 // only corrupt strings have a syntax error score 
        }

    fun part2() = lines
        .mapNotNull { line ->
            line.parse(
                onCorrupt = { null }, // combined with mapNotNull above, this discards corrupt strings
                onFinish = { it.toScore() } // converts the closing characters in the stack to a score
            )
        }.sorted() // sorts the scores
        .let { it[it.size / 2] } // returns the middle score
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