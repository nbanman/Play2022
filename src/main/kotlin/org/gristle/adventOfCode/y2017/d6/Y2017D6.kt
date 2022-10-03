package org.gristle.adventOfCode.y2017.d6

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D6(input: String) {

    private val initialState = input.split('\t').map { it.toInt() }

    private fun List<Int>.reallocate(): List<Int> {
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
    val c = Y2017D6(readRawInput("y2017/d6"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 12841
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 8038
}