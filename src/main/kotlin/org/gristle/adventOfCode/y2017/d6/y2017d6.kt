package org.gristle.adventOfCode.y2017.d6

import org.gristle.adventOfCode.utilities.*

object Y2017D6 {
    private val input = readRawInput("y2017/d6")

    val initialState = input.split('\t').map { it.toInt() }

    fun List<Int>.reallocate(): List<Int> {
        val alloc = maxOrNull() ?: throw NoSuchElementException("Empty list!")
        var index = indexOf(alloc)
        val newList = toMutableList()
        newList[index] = 0
        for (i in 1..alloc) {
            index = (index + 1) % size
            newList[index]++
        }
        return newList
    }

    fun part1(): Int {
        val states = mutableSetOf<List<Int>>()
        return generateSequence(initialState) {
            states.add(it)
            it.reallocate()
        }.indexOfFirst { states.contains(it) }
    }


    fun part2(): Int {
        val states = mutableSetOf<List<Int>>()
        var firstIndex = 0
        val matchIndex = generateSequence(initialState) {
            states.add(it)
            it.reallocate()
        }.indexOfFirst {
            if (states.contains(it)) {
                firstIndex = states.indexOf(it)
                true
            } else false
        }
        return matchIndex - firstIndex
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D6.part1()} (${elapsedTime(time)}ms)") // 12841
    time = System.nanoTime()
    println("Part 2: ${Y2017D6.part2()} (${elapsedTime(time)}ms)") // 8038
}