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

        val register = LongArray(6)
        val r1Set = mutableSetOf<Long>()
        var prior2 = 0L
        var prior5 = 0L
        while (true) {
            val command = commands[register[p].toInt()]
            command.execute(register)
            if (command.lineNo == 28) {
                if (part2) {
                    println("#2: ${register[1]}, Δ: ${register[1] - prior2}, #5: ${register[4]}, Δ: ${register[4] - prior5}")
                    prior2 = register[1]
                    prior5 = register[4]
                    if (r1Set.contains(register[1])) return r1Set.last()
                } else {
                    return register[1]
                }
                r1Set.add(register[1])
            }
        }
    }

    fun solve2(): Long {
        val register = LongArray(6)
        var function = 1

        fun p1(): Int {
            register[1] = 72
            return 6
        }

        fun p6(): Int {
            register[4] = register[1] or 65536
            register[1] = 16298264
            return 8
        }

        fun p8(): Int {
            register[5] = register[4] and 255
            register[1] = (register[1] + register[5]) and 16777215
            register[5] = 0
            return if (register[4] >= 256) 18 else 28
        }

        fun p18(): Int {
            return if (256 * (register[5] + 1) > register[4]) {
                register[4] = register[5]
                8
            } else {
                register[5]++
                18
            }
        }

        fun p28(): Int {
            return if (register[1] == register[0]) {
                99
            } else {
                6
            }
        }

        val r1Set = mutableSetOf<Long>()

        while (true) {
            function = when (function) {
                1 -> p1()
                6 -> p6()
                8 -> p8()
                18 -> p18()
                28 -> {
                    if (!r1Set.add(register[1])) return register[1]
                    println(register[1])
                    p28()
                }

                else -> 99
            }
        }
    }

    override fun part1() = solve()

    //    override fun part2() = solve(true)
    override fun part2() = solve2()
}

fun main() = Day.runDay(Y2018D21::class)

//    Class creation: 16ms
//    Part 1: 3345459 (90ms)
//    Part 2: 5857354 (34814ms)
//    Total time: 34921ms