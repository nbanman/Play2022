package org.gristle.adventOfCode.y2018.d1

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2018D1(input: String) {

    private val changes = input.lines().map { it.toInt() }

    fun part1() = changes.sum()

    fun part2(): Int {
        val states = mutableSetOf(0)
        tailrec fun change(freq: Int, changeList: List<Int>): Int {
            val newState = freq + changeList.first()
            return if (states.contains(newState)) {
                newState
            } else {
                states.add(newState)
                val newChanges = if (changeList.size == 1) changes else changeList.drop(1)
                change(newState, newChanges)
            }
        }
        return change(0, changes)
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