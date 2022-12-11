package org.gristle.adventOfCode.y2022.d11

import org.gristle.adventOfCode.utilities.*

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

        fun inspect(worryFactor: Int, lcm: Long) {
            inspections += items.size
            items.forEach { item ->
                val worryLevel = if (worryFactor == 3) item.op() / worryFactor else item.op() % lcm
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
                val name = gv[0].toInt()
                val starting = gv[1].getIntList().map(Int::toLong).toMutableList()
                val op = gv[2]
                val test = gv[3].toInt()
                val ifTrue = gv[4].toInt()
                val ifFalse = gv[5].toInt()
                Monkey(name, starting, op, test, ifTrue, ifFalse)
            }
        val lcm = lcm(monkeys.map { it.test.toLong() })
        repeat(rounds) { monkeys.forEach { monkey -> monkey.inspect(worryFactor, lcm) } }
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