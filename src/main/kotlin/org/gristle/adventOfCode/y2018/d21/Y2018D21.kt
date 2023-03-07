package org.gristle.adventOfCode.y2018.d21

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues

enum class Ops(val fn: (reg: LongArray, p: Int, a: Int, b: Int, c: Int) -> Unit) {
    ADDR({ reg, p, a, b, c -> reg.store(p, c, reg[a] + reg[b]) }),
    ADDI({ reg, p, a, b, c -> reg.store(p, c, reg[a] + b) }),
    MULR({ reg, p, a, b, c -> reg.store(p, c, reg[a] * reg[b]) }),
    MULI({ reg, p, a, b, c -> reg.store(p, c, reg[a] * b) }),
    BANR({ reg, p, a, b, c -> reg.store(p, c, reg[a] and reg[b]) }),
    BANI({ reg, p, a, b, c -> reg.store(p, c, reg[a] and b.toLong()) }),
    BORR({ reg, p, a, b, c -> reg.store(p, c, reg[a] or reg[b]) }),
    BORI({ reg, p, a, b, c -> reg.store(p, c, reg[a] or b.toLong()) }),
    SETR({ reg, p, a, _, c -> reg.store(p, c, reg[a]) }),
    SETI({ reg, p, a, _, c -> reg.store(p, c, a.toLong()) }),
    GTIR({ reg, p, a, b, c -> reg.store(p, c, if (a > reg[b]) 1 else 0) }),
    GTRI({ reg, p, a, b, c -> reg.store(p, c, if (reg[a] > b) 1 else 0) }),
    GTRR({ reg, p, a, b, c -> reg.store(p, c, if (reg[a] > reg[b]) 1 else 0) }),
    EQIR({ reg, p, a, b, c -> reg.store(p, c, if (a.toLong() == reg[b]) 1 else 0) }),
    EQRI({ reg, p, a, b, c -> reg.store(p, c, if (reg[a] == b.toLong()) 1 else 0) }),
    EQRR({ reg, p, a, b, c -> reg.store(p, c, if (reg[a] == reg[b]) 1 else 0) });

    companion object {
        fun from(s: String) = when (s) {
            "addr" -> ADDR
            "addi" -> ADDI
            "mulr" -> MULR
            "muli" -> MULI
            "banr" -> BANR
            "bani" -> BANI
            "borr" -> BORR
            "bori" -> BORI
            "setr" -> SETR
            "seti" -> SETI
            "gtir" -> GTIR
            "gtri" -> GTRI
            "gtrr" -> GTRR
            "eqir" -> EQIR
            "eqri" -> EQRI
            else -> EQRR
        }
    }
}

private fun LongArray.store(pointer: Int, location: Int, result: Long) {
    this[location] = result
    this[pointer]++
}

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

//    Class creation: 341513ms
//    Part 1: 3345459 (0ms)
//    Part 2: 5857354 (0ms)
//    Total time: 341513ms