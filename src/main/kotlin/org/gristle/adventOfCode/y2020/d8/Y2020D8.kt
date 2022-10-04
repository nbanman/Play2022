package org.gristle.adventOfCode.y2020.d8

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D8(input: String) {

    data class Instruction(val operation: String, val argument: Int)

    fun Instruction.execute() {
        when (operation) {
            "acc" -> { acc += argument; parser++ }
            "jmp" -> { parser += argument }
            else -> { parser++ }
        }
    }

    var acc = 0
    private var parser = 0

    private fun reset() {
        acc = 0
        parser = 0
    }

    val instructions = input
        .groupValues("""([a-z]{3}) \+?(-?\d+)""")
        .map { Instruction(it[0], it[1].toInt()) }

    fun part1(flippedIndex: Int = -1): Int {
        val pastStates = mutableSetOf<Int>()

        while (parser in instructions.indices) {
            if (pastStates.contains(parser)) return acc
            pastStates.add(parser)
            val current = instructions[parser]
            val fCurrent = if (parser == flippedIndex) {
                when (current.operation) {
                    "nop" -> Instruction("jmp", current.argument)
                    "jmp" -> Instruction("nop", instructions[parser].argument)
                    else -> current
                }
            } else current
            fCurrent.execute()
        }

        return acc
    }

    fun part2(): Int {
        for (flippedInstruction in instructions.indices) {
            if (instructions[flippedInstruction].operation == "acc") continue
            reset()
            val answer = part1(flippedInstruction)
            if (answer >= 0) return answer
        }
        return -1
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D8(readRawInput("y2020/d8"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1915
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 944
}