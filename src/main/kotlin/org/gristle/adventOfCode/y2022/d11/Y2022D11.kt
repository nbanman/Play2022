package org.gristle.adventOfCode.y2022.d11

import org.gristle.adventOfCode.utilities.*

/*
 * This works but is unsatisfactory. The Monkey class uses mutable data, has to be reset for every 
 * solve, uses a companion object to get the Monkeys to see each other, etc.
 * 
 * For a more satisfactory version, check out
 * https://github.com/ephemient/aoc2022/blob/main/kt/src/commonMain/kotlin/com/github/ephemient/aoc2022/Day11.kt
 * 
 * I started to refactor to get it in line with that, but his code is so to-the-point that it ended up being a 
 * straight copy. So I may as well just link to it and move on.
 */
class Y2022D11(val input: String) {

    class Monkey(
        val name: Int,
        private val items: MutableList<Long>,
        private val op: String,
        val test: Int,
        private val ifTrue: Int,
        private val ifFalse: Int
    ) {
        companion object {
            val monkeys = mutableMapOf<Int, Monkey>()
        }

        var inspections = 0

        init {
            monkeys[name] = this
        }

        private fun Long.op(): Long {
            val opValue = op.takeLastWhile { it != ' ' }.toLongOrNull() ?: this
            return when (op[0]) {
                '+' -> this + opValue
                else -> this * opValue
            }
        }

        fun inspect(worryReduction: Long.() -> Long) {
            inspections += items.size
            items.forEach { item ->
                val worryLevel = item.op().worryReduction()
                val receivingMonkey = if (worryLevel % test == 0L) ifTrue else ifFalse
                monkeys.getValue(receivingMonkey).items.add(worryLevel)
            }
            items.clear()
        }
    }

    private val pattern = """
        Monkey (\d+):
         +Starting items: (\d+(?:, \d+)*)
         +Operation: new = old ([+*] \w+)
         +Test: divisible by (\d+)
         +If true: throw to monkey (\d+)
         +If false: throw to monkey (\d+)
    """.trimIndent()

    fun solve(rounds: Int, worryFactor: Int): Long {
        val monkeys = input
            .groupValues(pattern)
            .map { gv ->
                Monkey(
                    name = gv[0].toInt(),
                    items = gv[1].getIntList().map(Int::toLong).toMutableList(),
                    op = gv[2],
                    test = gv[3].toInt(),
                    ifTrue = gv[4].toInt(),
                    ifFalse = gv[5].toInt()
                )
            }

        val lcm = if (worryFactor == 1) lcm(monkeys.map { it.test.toLong() }) else 0

        val worryReduction: (Long) -> Long = if (worryFactor == 1) {
            { worry: Long -> worry % lcm }
        } else {
            { worry: Long -> worry / 3 }
        }

        repeat(rounds) { monkeys.forEach { monkey -> monkey.inspect(worryReduction) } }
        return monkeys.map(Monkey::inspections).sortedDescending().take(2).fold(1L, Long::times)
    }

    fun part1() = solve(20, 3)

    fun part2() = solve(10000, 1)
}

fun main() {
    val input = getInput(11, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D11(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 88208
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 21115867968
    println("Total time: ${timer.elapsed()}ms")
}