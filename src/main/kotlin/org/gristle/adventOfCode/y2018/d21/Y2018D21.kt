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
        fun execute(reg: List<Long>) = op.fn(reg, p, a, b, c)
    }

    fun solve(): Pair<Long, Long> {
        val data = input
        val p = data.takeWhile { it !in "\r\n" }.takeLast(1).toInt()
        val commands = data
            .groupValues(pattern)
            .mapIndexed { index, gv ->
                val ops = when (gv[0]) {
                    "addr" -> Ops.ADDR
                    "addi" -> Ops.ADDI
                    "mulr" -> Ops.MULR
                    "muli" -> Ops.MULI
                    "banr" -> Ops.BANR
                    "bani" -> Ops.BANI
                    "borr" -> Ops.BORR
                    "bori" -> Ops.BORI
                    "setr" -> Ops.SETR
                    "seti" -> Ops.SETI
                    "gtir" -> Ops.GTIR
                    "gtri" -> Ops.GTRI
                    "gtrr" -> Ops.GTRR
                    "eqir" -> Ops.EQIR
                    "eqri" -> Ops.EQRI
                    else -> Ops.EQRR
                }
                Command(ops, p, gv[1].toInt(), gv[2].toInt(), gv[3].toInt(), index)
            }

        var register = listOf(0L, 0L, 0L, 0L, 0L, 0L)
        val r1Set = mutableSetOf<Long>()
        var p1 = 0L
        while (true) {
            val command = commands[register[p].toInt()]
            register = command
                .execute(register)
            if (command.lineNo == 28) {
                if (r1Set.isEmpty()) p1 = register[1]
                if (r1Set.contains(register[1])) {
                    return p1 to r1Set.last()
                } else r1Set.add(register[1])
            }
        }
    }

    val solution = solve()

    override fun part1() = solution.first

    override fun part2() = solution.second
}

fun main() = Day.runDay(Y2018D21::class)

//    Class creation: 341513ms
//    Part 1: 3345459 (0ms)
//    Part 2: 5857354 (0ms)
//    Total time: 341513ms