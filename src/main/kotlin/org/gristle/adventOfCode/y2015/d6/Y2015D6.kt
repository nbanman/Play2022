package org.gristle.adventOfCode.y2015.d6

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import kotlin.math.max

class Y2015D6(input: String) : Day {

    data class Instruction(val command: Int, val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
        companion object {
            fun of(line: String): Instruction {
                val command = when {
                    line.startsWith("turn on") -> 1
                    line.startsWith("turn off") -> -1
                    line.startsWith("toggle") -> 0
                    else -> throw IllegalArgumentException("Error parsing input: $line")
                }
                val (x1, y1, x2, y2) = line.getIntList()
                return Instruction(command, x1, y1, x2, y2)
            }
        }
    }

    val instructions = input
        .lineSequence()
        .map(Instruction::of)
        .toList()

    val length = 1_000

    override fun part1(): Int {
        val lights = BooleanArray(length * length)
        fun BooleanArray.execute(instruction: Instruction) {
            for (y in instruction.y1..instruction.y2) for (x in instruction.x1..instruction.x2) {
                val index = y * length + x
                this[index] = when (instruction.command) {
                    1 -> true
                    -1 -> false
                    else -> !this[index]
                }
            }
        }
        instructions.forEach { lights.execute(it) }
        return lights.count { it }
    }

    override fun part2(): Int {
        val lights = IntArray(length * length)
        fun IntArray.execute(instruction: Instruction) {
            for (y in instruction.y1..instruction.y2) for (x in instruction.x1..instruction.x2) {
                val index = y * length + x
                this[index] = when (instruction.command) {
                    1 -> this[index] + 1
                    -1 -> max(0, this[index] - 1)
                    else -> this[index] + 2
                }
            }
        }
        instructions.forEach { lights.execute(it) }
        return lights.sum()
    }
}

fun main() = Day.runDay(Y2015D6::class)

//    Class creation: 28ms
//    Part 1: 569999 (34ms)
//    Part 2: 17836115 (37ms)
//    Total time: 100ms