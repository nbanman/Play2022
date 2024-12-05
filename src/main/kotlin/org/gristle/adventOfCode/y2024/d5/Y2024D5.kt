package org.gristle.adventOfCode.y2024.d5

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.getIntList

class Y2024D5(input: String) : Day {
    private val updates: List<List<Int>>
    private val leftOf: Map<Int, Set<Int>>
    init {
        val (rules, updates) = input.blankSplit().map { stanza ->
            stanza.lines().map { it.getIntList() }
        }
        val leftOf = mutableMapOf<Int, MutableSet<Int>>()
        for ((left, right) in rules) {
            leftOf.getOrPut(right) { mutableSetOf() }.add(left)
            leftOf.getOrPut(left) { mutableSetOf() }
        }
        this.updates = updates
        this.leftOf = leftOf
    }

    private fun updateSort(update: Set<Int>): List<Int> = leftOf
        .mapNotNull { (page, onLeft) ->
            if (page in update) {
                page to onLeft.intersect(update).size
            } else {
                null
            }
        }.sortedBy { (_, pagesOnLeft) -> pagesOnLeft }
        .map { (page, _) -> page }

    override fun part1(): Int = updates.sumOf { update ->
        val sorted = updateSort(update.toSet())
        if (update == sorted) {
            update[update.size / 2]
        } else {
            0
        }
    }

    override fun part2(): Int = updates.sumOf { update ->
        val sorted = updateSort(update.toSet())
        if (update != sorted) {
            sorted[update.size / 2]
        } else {
            0
        }
    }
}

fun main() = Day.runDay(Y2024D5::class)

//    Class creation: 24ms
//    Part 1: 5129 (18ms)
//    Part 2: 4077 (9ms)
//    Total time: 52ms