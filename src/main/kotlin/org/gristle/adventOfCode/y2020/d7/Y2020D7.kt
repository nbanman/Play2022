package org.gristle.adventOfCode.y2020.d7

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues
import java.util.*

class Y2020D7(input: String) : Day {

    class HeldBag(val color: String, private val amount: Int) {
        fun bagsInside(): Long {
            return amount + amount * Rule.bagMap.getValue(color).heldBags.sumOf(HeldBag::bagsInside)
        }
    }

    class Rule(private val color: String, val heldBags: List<HeldBag>) {
        companion object {
            val bagMap = mutableMapOf<String, Rule>()
        }

        init {
            bagMap[color] = this
        }

        fun contains(other: String): Boolean {
            val visited = mutableSetOf(color)
            val q: Deque<HeldBag> = LinkedList()
            q.addAll(heldBags)
            while (q.isNotEmpty()) {
                val current = q.poll().color
                if (current == other) return true
                visited.add(current)
                q.addAll(bagMap.getValue(current).heldBags.filter { !visited.contains(it.color) })
            }
            return false
        }

        fun bagsInside(): Long {
            return HeldBag(color, 1).bagsInside() - 1
        }
    }

    private var rules = input
        .groupValues("""(\w+ \w+) bags contain ([^.]+).""")
        .map { gv ->
            val heldBags = gv[1]
                .groupValues("""(\d+) (\w+ \w+) bag""")
                .map { HeldBag(it[1], it[0].toInt()) }
            Rule(gv[0], heldBags)
        }

    override fun part1() = rules.count { it.contains("shiny gold") }

    override fun part2() = Rule.bagMap.getValue("shiny gold").bagsInside()
}

fun main() = Day.runDay(Y2020D7::class)

//    Class creation: 56ms
//    Part 1: 252 (17ms)
//    Part 2: 35487 (0ms)
//    Total time: 74ms