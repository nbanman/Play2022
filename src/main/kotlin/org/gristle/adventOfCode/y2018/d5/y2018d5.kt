package org.gristle.adventOfCode.y2018.d5

import org.gristle.adventOfCode.utilities.*
import java.util.*
import kotlin.math.abs

object Y2018D5 {
    private val input = readRawInput("y2018/d5")

    private fun String.react() = LinkedList<Char>()
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
    println("Part 1: ${Y2018D5.part1()} (${elapsedTime(time)}ms)") // 10972 
    time = System.nanoTime()
    println("Part 2: ${Y2018D5.part2()} (${elapsedTime(time)}ms)") // 5278
}