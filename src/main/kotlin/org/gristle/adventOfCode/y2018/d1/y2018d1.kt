package org.gristle.adventOfCode.y2018.d1

import org.gristle.adventOfCode.utilities.*

object Y2018D1 {
    private val input = readInput("y2018/d1")

    private val changes = input.map { it.toInt() }

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
    println("Part 1: ${Y2018D1.part1()} (${elapsedTime(time)}ms)") // 433
    time = System.nanoTime()
    println("Part 2: ${Y2018D1.part2()} (${elapsedTime(time)}ms)") // 256
}