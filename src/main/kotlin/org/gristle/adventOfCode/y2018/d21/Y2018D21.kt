package org.gristle.adventOfCode.y2018.d21

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.y2018.shared.Ops

class Y2018D21(private val input: String) : Day {
    private val pattern = """([a-z]{4}) (\d+) (\d+) (\d+)""".toRegex()

    data class Command(
        val op: Ops,
        val p: Int,
        val a: Int,
        val b: Int,
        val c: Int,
        val lineNo: Int,
    ) {
        fun execute(reg: LongArray) = op.fn(reg, p, a, b, c)
    }

    fun solve(part2: Boolean = false): Long {
        val p = input.takeWhile { it !in "\r\n" }.takeLast(1).toInt()
        val commands = input
            .groupValues(pattern)
            .mapIndexed { index, gv ->
                Command(Ops.from(gv[0]), p, gv[1].toInt(), gv[2].toInt(), gv[3].toInt(), index)
            }

        val register = longArrayOf(0L, 0L, 0L, 0L, 0L, 0L)
        val r1Set = mutableSetOf<Long>()
        while (true) {
            val command = commands[register[p].toInt()]
            command.execute(register)
            if (command.lineNo == 28) {
                if (part2) {
                    if (r1Set.contains(register[1])) return r1Set.last()
                } else {
                    return register[1]
                }
                r1Set.add(register[1])
            }
        }
    }

    override fun part1() = solve()

    override fun part2() = solve(true)
}

fun main() = Day.runDay(Y2018D21::class)

//    Class creation: 16ms
//    Part 1: 3345459 (90ms)
//    Part 2: 5857354 (34814ms)
//    Total time: 34921ms