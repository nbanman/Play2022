package org.gristle.adventOfCode.y2016.d12

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

data class Assembunny(val type: String, val arg1: String, val arg2: String, var toggled: Boolean = false) {
    fun tgl() {
        toggled = !toggled
    }
}

data class Registers(var a: Int = 0, var b: Int = 0, var c: Int = 0, var d: Int = 0) {
    fun valueOf(register: String) = when (register) {
        "a" -> a
        "b" -> b
        "c" -> c
        "d" -> d
        else -> null
    }
    fun updateValue(register: String, value: Int) {
        when (register) {
            "a" -> a = value
            "b" -> b = value
            "c" -> c = value
            "d" -> d = value
        }
    }
    fun incRegister(register: String, multiply: Boolean) {
        when (register) {
            "a" -> a = if (multiply) a * 2 else a + 1
            "b" -> b = if (multiply) b * 2 else b + 1
            "c" -> c = if (multiply) c * 2 else c + 1
            "d" -> d = if (multiply) d * 2 else d + 1
        }
    }

    fun decRegister(register: String) {
        when (register) {
            "a" -> a--
            "b" -> b--
            "c" -> c--
            "d" -> d--
        }
    }

}

class Y2016D12(input: String) {

    private val pattern = """(\w+) (-?\w+)(?: (-?\w+))?""".toRegex()

    val instructions = input
        .groupValues(pattern)
        .map { Assembunny(it[0], it[1], it[2]) }

    private val p1Registers = Registers()
    private val p2Registers = Registers().apply { updateValue("c", 1) }

    fun part1() = runInstructions(instructions, p1Registers).a

    fun part2() = runInstructions(instructions, p2Registers).a
}

fun runInstructions(
    instructions: List<Assembunny>,
    registers: Registers,
    multiply: Boolean = false
): Registers {
    var i = 0
    while (i in instructions.indices) {
        val instruction = instructions[i]
        when (instruction.type) {
            "cpy" -> {
                if (instruction.toggled) {
                    if (registers.valueOf(instruction.arg1) != 0) {
                        i += registers.valueOf(instruction.arg2) ?: instruction.arg2.toInt()
                        continue
                    }
                } else {
                    val number = registers.valueOf(instruction.arg1) ?: instruction.arg1.toInt()
                    registers.updateValue(instruction.arg2, number)
                }
            }
            "inc" -> {
                if (instruction.toggled) {
                    registers.decRegister(instruction.arg1)
                } else {
                    registers.incRegister(instruction.arg1, multiply)
                }

            }
            "dec" -> {
                if (instruction.toggled) {
                    registers.incRegister(instruction.arg1, multiply)
                } else {
                    registers.decRegister(instruction.arg1)
                }

            }
            "jnz" -> {
                if (instruction.toggled) {
                    val number = registers.valueOf(instruction.arg1) ?: instruction.arg1.toInt()
                    registers.updateValue(instruction.arg2, number)
                } else {
                    if (registers.valueOf(instruction.arg1) != 0) {
                        i += registers.valueOf(instruction.arg2) ?: instruction.arg2.toInt()
                        continue
                    }
                }
            }
            "tgl" -> {
                if (instruction.toggled) {
                    registers.incRegister(instruction.arg1, multiply)
                } else {
                    val index = i + (registers.valueOf(instruction.arg1) ?: throw IllegalStateException())
                    if (index in instructions.indices) instructions[index].tgl()
                }
            }
            "out" -> {
                val number = registers.valueOf(instruction.arg1) ?: instruction.arg1.toInt()
                // if ((clockZero && number == 0) || (!clockZero && number == 1)) println(number) else break
                println (number)
            }
        }
        i++
    }
    return registers
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D12(readRawInput("y2016/d12"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 318117
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 9227771
}