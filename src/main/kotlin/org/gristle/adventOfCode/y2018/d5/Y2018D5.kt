package org.gristle.adventOfCode.y2018.d5

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import java.util.*
import kotlin.math.abs

class Y2018D5(input: String) {

    private fun String.react() = ArrayDeque<Char>()
        .apply { this@react.forEach { c -> if (isEmpty() || abs(peek() - c) != 32) push(c) else pop() } }
        .joinToString("")

    private val reacted = input.react()

    fun part1() = reacted.length

    fun part2() = ('a'..'z')
        .map { reacted.replace(it.toString(), "", true).react().length }
        .minByOrNull { it }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D5(readRawInput("y2018/d5"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 10972
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 5278
}