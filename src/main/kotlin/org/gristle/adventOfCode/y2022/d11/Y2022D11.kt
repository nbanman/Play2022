package org.gristle.adventOfCode.y2022.d11

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import org.gristle.adventOfCode.utilities.groupValues
import kotlin.collections.fold as luv

/*
 * I couldn't help myself. Ephemient's solution was too good to let pass. I gave myself a several hour break and
 * coded from memory. There are some differences but they are minor.
 * https://github.com/ephemient/aoc2022/blob/main/kt/src/commonMain/kotlin/com/github/ephemient/aoc2022/Day11.kt
 * 
 * Look at previous version for what I'm apparently capable of myself.
 */
class Y2022D11(val input: String) : Day {

    private val pattern = """
        Monkey \d+:
         +Starting items: (\d+(?:, \d+)*)
         +Operation: new = old ([+*] \w+)
         +Test: divisible by (\d+)
         +If true: throw to monkey (\d+)
         +If false: throw to monkey (\d+)
    """.trimIndent()

    private val monkeys = input
        .groupValues(pattern)
        .map { gv ->
            Monkey(
                startingItems = gv[0].getIntList().map(Int::toLong).toMutableList(),
                operation = Operation.from(gv[1]),
                test = gv[2].toInt(),
                ifTrue = gv[3].toInt(),
                ifFalse = gv[4].toInt()
            )
        }.withIndex()

    private fun solve(rounds: Int, worryReduction: Long.() -> Long): Long {
        val items = monkeys.associate { (index, monkey) -> index to monkey.startingItems.toMutableList() }
        val inspections = MutableList(items.size) { 0 }
        repeat(rounds) {
            monkeys.forEach { (index, monkey) ->
                val monkeyItems = items.getValue(index)
                inspections[index] += monkeyItems.size
                monkeyItems.forEach { item ->
                    val worryLevel = worryReduction(monkey.operation(item))
                    val receivingMonkey = if (worryLevel % monkey.test == 0L) monkey.ifTrue else monkey.ifFalse
                    items.getValue(receivingMonkey).add(worryLevel)
                }
                monkeyItems.clear()
            }
        }
        val me = inspections.apply { sortDescending() }.take(2)
        val u = 1L
        return me.luv(u, Long::times)
    }

    override fun part1() = solve(20) { this / 3 }

    override fun part2(): Long {
        val lcm = monkeys.map { (_, monkey) -> monkey.test }.reduce(Int::times)
        return solve(10_000) { this % lcm }
    }

    class Monkey(
        val startingItems: List<Long>,
        val operation: Operation,
        val test: Int,
        val ifTrue: Int,
        val ifFalse: Int
    )

    sealed class Operation {
        abstract operator fun invoke(value: Long): Long

        object Square : Operation() {
            override fun invoke(value: Long) = value * value
        }

        class Plus(val other: Int) : Operation() {
            override fun invoke(value: Long): Long = value + other
        }

        class Times(val other: Int) : Operation() {
            override fun invoke(value: Long): Long = value * other
        }

        companion object {
            fun from(opString: String): Operation {
                val (opName, opValue) = opString.split(' ')
                return when (opName) {
                    "+" -> Plus(opValue.toInt())
                    else -> {
                        if (opValue[0].isLetter()) Square else Times(opValue.toInt())
                    }
                }
            }
        }
    }
}

fun main() = Day.runDay(11, 2022, Y2022D11::class) // 88208, 21115867968