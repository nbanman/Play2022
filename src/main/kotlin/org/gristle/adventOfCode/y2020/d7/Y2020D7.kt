package org.gristle.adventOfCode.y2020.d7

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues
import java.util.*

class Y2020D7(input: String) : Day {

    class HeldBag(val color: String, private val amount: Int) {
        fun bagsInside(bagMap: Map<String, Rule>): Int {
            return amount + amount * bagMap.getValue(color).heldBags.sumOf { it.bagsInside(bagMap) }
        }
    }

    class Rule(val color: String, val heldBags: List<HeldBag>) {

        fun contains(other: String, bagMap: Map<String, Rule>): Boolean {
            val visited = mutableSetOf(color)
            val q: Deque<HeldBag> = ArrayDeque()
            q.addAll(heldBags)
            while (q.isNotEmpty()) {
                val current = q.poll().color
                if (current == other) return true
                visited.add(current)
                q.addAll(bagMap.getValue(current).heldBags.filter { !visited.contains(it.color) })
            }
            return false
        }

        fun bagsInside(bagMap: Map<String, Rule>): Int {
            return HeldBag(color, 1).bagsInside(bagMap) - 1
        }
    }

    private val rules = input
        .groupValues("""(\w+ \w+) bags contain ([^.]+).""")
        .map { gv ->
            val heldBags = gv[1]
                .groupValues("""(\d+) (\w+ \w+) bag""")
                .map { HeldBag(it[1], it[0].toInt()) }
            Rule(gv[0], heldBags)
        }

    private val bagMap = buildMap {
        rules.forEach { rule ->
            put(rule.color, rule)
        }
    }

    override fun part1() = rules.count { it.contains("shiny gold", bagMap) }

    override fun part2() = bagMap.getValue("shiny gold").bagsInside(bagMap)
}

fun main() = Day.runDay(Y2020D7::class)

//    Class creation: 56ms
//    Part 1: 252 (17ms)
//    Part 2: 35487 (0ms)
//    Total time: 74ms