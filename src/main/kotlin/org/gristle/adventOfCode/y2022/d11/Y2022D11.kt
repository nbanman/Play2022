package org.gristle.adventOfCode.y2022.d11

import org.gristle.adventOfCode.utilities.*

class Y2022D11(val input: String) {

    data class Monkey(
        val name: Int,
        val items: MutableList<Long>,
        val op: String,
        val test: Int,
        val ifTrue: Int,
        val ifFalse: Int
    ) {
        companion object {
            val monkeys = mutableMapOf<Int, Monkey>()
        }

        var inspections = 0

        init {
            monkeys[name] = this
        }

        fun Long.op(): Long {
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

        override fun toString(): String {
            return "Monkey(name=$name, items=$items, op='$op', test=$test, ifTrue=$ifTrue, ifFalse=$ifFalse, inspections=$inspections)"
        }
    }

    private val pattern = Regex(
        """Monkey (\d+):
  Starting items: (\d+(?:, \d+)*)
  Operation: new = old ([+*] \w+)
  Test: divisible by (\d+)
    If true: throw to monkey (\d+)
    If false: throw to monkey (\d+)"""
    )

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
    val input = listOf(
        getInput(11, 2022),
        """Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1""",
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D11(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 88208
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 21115867968
    println("Total time: ${timer.elapsed()}ms")
}