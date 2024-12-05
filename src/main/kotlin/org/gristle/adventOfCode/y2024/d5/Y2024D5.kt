package org.gristle.adventOfCode.y2024.d5

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.getIntList

class Y2024D5(input: String) : Day {
    val rules: List<List<Int>>
    val updates: List<List<Int>>

    init {
        val (rules, updates) = input.blankSplit().map { stanza ->
            stanza.lines().map { it.getIntList() }
        }
        this.rules = rules
        this.updates = updates
    }
    val validPages = rules.flatten().toSet()
    val leftOf: Map<Int, Set<Int>>
    val rightOf: Map<Int, Set<Int>>

    init {
        val leftOf = mutableMapOf<Int, MutableSet<Int>>()
        val rightOf = mutableMapOf<Int, MutableSet<Int>>()
        for ((left, right) in rules) {
            leftOf.getOrPut(left) { mutableSetOf() }.add(right)
            rightOf.getOrPut(right) { mutableSetOf() }.add(left)
        }
        this.leftOf = leftOf
        this.rightOf = rightOf
    }

    fun Int.onSideOf(index: Map<Int, Set<Int>>): Set<Int> {
        val q = mutableListOf(this)
        val onSide = mutableSetOf<Int>()
        while (q.isNotEmpty()) {
            val current = q.removeLast()
            onSide.add(current)
            q.addAll((index[current] ?: emptySet()).filter { it !in onSide })
        }
        onSide.remove(this)
        return onSide
    }

    override fun part1(): Int {
        return updates
            .filter { update ->
                update.withIndex().all { (idx, page) ->
                    val toTheLeft = update.subList(0, idx)
                    val toTheRight = update.subList(idx + 1, update.size)
                    val onLeft = page.onSideOf(rightOf)
                    val onRight = page.onSideOf(leftOf)
                    if (toTheLeft.any { it !in onLeft }) {
                        false
                    } else if (toTheRight.any { it !in onRight }) {
                        false
                    } else {
                        true
                    }
                }
            }.sumOf { update -> update[update.size / 2] }
    }
    override fun part2() = "part 2"
}
// 143,
// 9650 too high
fun main() = Day.runDay(Y2024D5::class)//, test[0])

private val test = listOf("""47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47""")