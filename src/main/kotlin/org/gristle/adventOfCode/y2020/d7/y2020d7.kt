package org.gristle.adventOfCode.y2020.d7

import org.gristle.adventOfCode.utilities.*
import java.util.*

object Y2020D7 {
    private val input = readRawInput("y2020/d7")

    class HeldBag(val color: String, val amount: Int) {
        fun bagsInside(): Long {
            return amount + amount * Rule.bagMap[color]!!.heldBags.sumOf { it.bagsInside() }
        }
    }
    class Rule(val color: String, val heldBags: List<HeldBag>) {
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
                q.addAll(bagMap[current]!!.heldBags.filter { !visited.contains(it.color) })
            }
            return false
        }

        fun bagsInside(): Long {
            return HeldBag(color, 1).bagsInside() - 1
        }
    }

    var rules = input
        .groupValues("""(\w+ \w+) bags contain ([^.]+).""")
        .map { gv ->
            val heldBags = gv[1]
                .groupValues("""(\d+) (\w+ \w+) bag""")
                .map { HeldBag(it[1], it[0].toInt()) }
            Rule(gv[0], heldBags)
        }

    fun part1() = rules.count { it.contains("shiny gold") }

    fun part2() = Rule.bagMap["shiny gold"]!!.bagsInside()
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D7.part1()} (${elapsedTime(time)}ms)") // 252
    time = System.nanoTime()
    println("Part 2: ${Y2020D7.part2()} (${elapsedTime(time)}ms)") // 35487
}