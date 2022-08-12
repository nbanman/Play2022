package org.gristle.adventOfCode.y2015.d7

import org.gristle.adventOfCode.utilities.*
import kotlin.math.pow

object Y2015D7 {
    private val input = readRawInput("y2015/d7")

    sealed class Instruction(val arg1: String, val wire: String) {
        companion object {
            val register = mutableMapOf<String, Int>()
        }

        class Assign(arg1: String, wire: String) : Instruction(arg1, wire) {
            override fun execute() = valueOf(arg1)
        }

        class And(arg1: String, private val arg2: String, wire: String) : Instruction(arg1, wire) {
            override fun execute(): Int {
                val a1 = valueOf(arg1)
                val a2 = valueOf(arg2)
                return if (a1 == -1 || a2 == -1) -1 else a1 and a2
            }
        }

        class Not(arg1: String, wire: String) : Instruction(arg1, wire) {
            override fun execute(): Int {
                val arg1Value = valueOf(arg1)
                if (arg1Value == -1) return -1
                return (0..15).fold(0) { acc, i ->
                    val complement = (arg1Value.shr(i) and 1).let { if (it == 1) 0 else 1 }
                    acc + complement * 2.0.pow(i).toInt()
                }
            }
        }

        class Or(arg1: String, private val arg2: String, wire: String) : Instruction(arg1, wire) {
            override fun execute(): Int {
                val a1 = valueOf(arg1)
                val a2 = valueOf(arg2)
                return if (a1 == -1 || a2 == -1) -1 else a1 or a2
            }
        }

        class LShift(arg1: String, private val shift: Int, wire: String) : Instruction(arg1, wire) {
            override fun execute(): Int {
                val a1 = valueOf(arg1)
                return if (a1 == -1) -1 else a1 shl shift
            }
        }

        class RShift(arg1: String, private val shift: Int, wire: String) : Instruction(arg1, wire) {
            override fun execute(): Int {
                val a1 = valueOf(arg1)
                return if (a1 == -1) -1 else a1 shr shift
            }
        }

        abstract fun execute(): Int

        fun registerWire(): Boolean {
            val execution = execute()
            return if (execution == -1) false else {
                register[wire] = execution
                true
            }
        }

        fun valueOf(s: String): Int {
            val numericValue = s.toIntOrNull()
            return numericValue ?: register[s] ?: -1
        }
    }

    private const val pattern = """(?:(\w+) )?(?:(AND|NOT|OR|LSHIFT|RSHIFT) )?(\w+) -> ([a-z]+)"""

    private val instructions = input
        .groupValues(pattern)
        .map { gv ->
            when {
                gv[1] == "AND" -> Instruction.And(gv[0], gv[2], gv[3])
                gv[0] == "NOT" -> Instruction.Not(gv[2], gv[3])
                gv[1] == "OR" -> Instruction.Or(gv[0], gv[2], gv[3])
                gv[1] == "LSHIFT" -> Instruction.LShift(gv[0], gv[2].toInt(), gv[3])
                gv[1] == "RSHIFT" -> Instruction.RShift(gv[0], gv[2].toInt(), gv[3])
                else -> Instruction.Assign(gv[2], gv[3])
            }
        }

    fun part1(ignoreB: Boolean = false): Int {
        val insts = instructions
            .filter { if (!ignoreB) true else it.wire != "b" }
            .toMutableList()

        val executed = mutableListOf<Instruction>()
        while (insts.isNotEmpty()) {
            insts.forEach { if (it.registerWire()) executed.add(it) }
            insts.removeAll(executed)
            executed.clear()
        }
        return Instruction.register["a"] ?: -1
    }

    fun part2(): Int {
        val a = Instruction.register["a"] ?: -1
        Instruction.register.clear()
        Instruction.register["b"] = a
        return part1(true)
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D7.part1()} (${elapsedTime(time)}ms)") // 46065
    time = System.nanoTime()
    println("Part 2: ${Y2015D7.part2()} (${elapsedTime(time)}ms)") // 14134
}