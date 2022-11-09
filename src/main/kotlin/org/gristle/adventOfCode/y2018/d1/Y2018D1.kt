package org.gristle.adventOfCode.y2018.d1

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2018D1(input: String) {

    private val changes = input.lines().map(String::toInt)

    fun part1() = changes.sum()

    fun part2(): Int {
        val states = mutableSetOf(0)
        tailrec fun change(freq: Int, index: Int): Int {
            val newState = freq + changes[index]
            return if (states.contains(newState)) {
                newState
            } else {
                states.add(newState)
                change(newState, (index + 1) % changes.size)
            }
        }
        return change(0, 0)
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D1(readRawInput("y2018/d1"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 433
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 256
}