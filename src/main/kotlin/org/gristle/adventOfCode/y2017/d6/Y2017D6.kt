package org.gristle.adventOfCode.y2017.d6

import org.gristle.adventOfCode.Day

class Y2017D6(input: String) : Day {

    private val initialState = input.split('\t').map { it.toInt() }

    private fun List<Int>.reallocate(): List<Int> {
        val (index, alloc) = withIndex().maxBy { it.value }
        val newList = toMutableList()
        newList[index] = 0
        for (i in 1..alloc) {
            newList[(index + i) % size]++
        }
        return newList
    }

    private fun allocationSequence() =
        generateSequence(mutableSetOf<List<Int>>() to initialState) { (set, last) ->
            set.apply { add(last) } to last.reallocate()
        }

    override fun part1(): Int = allocationSequence()
        .indexOfFirst { (set, last) -> last in set }

    override fun part2(): Int = allocationSequence()
        .first { (set, last) -> last in set }
        .let { (set, last) -> set.size - set.indexOf(last) }
}

fun main() = Day.runDay(Y2017D6::class)

//    Class creation: 5ms
//    Part 1: 12841 (20ms)
//    Part 2: 8038 (13ms)
//    Total time: 39ms
