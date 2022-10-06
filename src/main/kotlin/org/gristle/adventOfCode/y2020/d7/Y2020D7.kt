package org.gristle.adventOfCode.y2020.d7

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import java.util.*

class Y2020D7(input: String) {

    class HeldBag(val color: String, private val amount: Int) {
        fun bagsInside(): Long {
            return amount + amount * Rule.bagMap.getValue(color).heldBags.sumOf { it.bagsInside() }
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

    fun part1() = rules.count { it.contains("shiny gold") }

    fun part2() = Rule.bagMap.getValue("shiny gold").bagsInside()
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D7(readRawInput("y2020/d7"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 252
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 35487
}