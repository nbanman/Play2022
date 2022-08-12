package org.gristle.adventOfCode.y2020.d14

import org.gristle.adventOfCode.utilities.*

object Y2020D14 {
    private val input = readInput("y2020/d14")

    sealed class Instruction {

        companion object {
            fun fromString(s: String): Instruction {
                val gv = Regex("""(mask|mem)(?:\[(\d+)])? = ([X0-9]+)""")
                    .find(s)!!
                    .groupValues
                return when (gv[1]) {
                    "mask" -> {
                        val oneMask = gv[3].foldRightIndexed(0L) { index, c, acc ->
                            acc + if (c == '1') { 1L.shl(gv[3].length - index - 1) } else 0L
                        }
                        val zeroMask = gv[3].foldRightIndexed(0L) { index, c, acc ->
                            acc + if (c != '0') { 1L.shl(gv[3].length - index - 1) } else 0L
                        }
                        val xMask = gv[3].foldRightIndexed(0L) { index, c, acc ->
                            acc + if (c == 'X') { 1L.shl(gv[3].length - index - 1) } else 0L
                        }
                        Mask(oneMask, zeroMask, xMask)
                    }
                    else -> {
                        Mem(gv[2].toLong(), gv[3].toLong())
                    }
                }
            }
        }

        data class Mask internal constructor(val oneMask: Long, val zeroMask: Long, val xMask: Long) : Instruction()

        data class Mem(val register: Long, val value: Long) : Instruction() {
            fun maskedRegisters(oneMask: Long, xMask: Long): List<Long> {
                val oneApplied = register.or(oneMask)
                return (0..35).fold(listOf(0L)) { acc, place ->
                    when {
                        1L.and(xMask.shr(place)) == 1L -> {
                            acc.flatMap { listOf(it, it + 1L.shl(place)) }
                        }
                        1L.and(oneApplied.shr(place)) == 1L -> {
                            acc.map { it + 1L.shl(place) }
                        }
                        else -> {
                            acc
                        }
                    }
                }
            }

            fun maskedValue(oneMask: Long, zeroMask: Long): Long {
                return value.or(oneMask).and(zeroMask)
            }
        }
    }

    private val instructions = input
        .map { Instruction.fromString(it) }

    fun part1(): Long {
        val registers = mutableMapOf<Long, Long>()
        var oneMask = 0L
        var zeroMask = 0L
        instructions.forEach { instruction ->
            when (instruction) {
                is Instruction.Mask -> {
                    oneMask = instruction.oneMask
                    zeroMask = instruction.zeroMask
                }
                is Instruction.Mem -> {
                    registers[instruction.register] = instruction.maskedValue(oneMask, zeroMask)
                }
            }
        }
        return registers.values.sum()
    }

    fun part2(): Long {
        val registers = mutableMapOf<Long, Long>()
        var oneMask = 0L
        var xMask = 0L
        instructions.forEach { instruction ->
            when (instruction) {
                is Instruction.Mask -> {
                    oneMask = instruction.oneMask
                    xMask = instruction.xMask
                }
                is Instruction.Mem -> {
                    instruction.maskedRegisters(oneMask, xMask).forEach {
                        registers[it] = instruction.value
                    }
                }
            }
        }
        return registers.values.sum()
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D14.part1()} (${elapsedTime(time)}ms)") // 11926135976176 
    time = System.nanoTime()
    println("Part 2: ${Y2020D14.part2()} (${elapsedTime(time)}ms)") // 4330547254348
}