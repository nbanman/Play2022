package org.gristle.adventOfCode.y2018.d5

import org.gristle.adventOfCode.Day
import java.util.*
import kotlin.math.abs

class Y2018D5(input: String) : Day {

    private fun String.react() = ArrayDeque<Char>()
        .also { forEach { c -> if (it.isEmpty() || abs(it.peek() - c) != 32) it.push(c) else it.pop() } }
        .joinToString("")

    private val reacted = input.react()

    override fun part1() = reacted.length

    override fun part2() = ('a'..'z')
        .map { reacted.replace(it.toString(), "", true).react().length }
        .minByOrNull { it }
}

fun main() = Day.runDay(Y2018D5::class)

//    Class creation: 30ms
//    Part 1: 10972 (0ms)
//    Part 2: 5278 (34ms)
//    Total time: 64ms