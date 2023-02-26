package org.gristle.adventOfCode.y2017.d6

import org.gristle.adventOfCode.Day

class Y2017D6(input: String) : Day {

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

    override fun part1(): Int {
        val states = mutableSetOf<List<Int>>()
        return generateSequence(initialState) {
            states.add(it)
            it.reallocate()
        }.indexOfFirst { states.contains(it) }
    }

    override fun part2(): Int {
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

fun main() = Day.runDay(6, 2017, Y2017D6::class)

//    Class creation: 5ms
//    Part 1: 12841 (20ms)
//    Part 2: 8038 (13ms)
//    Total time: 39ms
