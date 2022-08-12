package org.gristle.adventOfCode.y2016.d12

import org.gristle.adventOfCode.utilities.*

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

object Y2016D12 {
    private val input = readRawInput("y2016/d12")

    private val pattern = """(\w+) (-?\w+)(?: (-?\w+))?"""

    fun runInstructions(
        instructions: List<Assembunny>,
        registers: Registers,
        multiply: Boolean = false
    ): Registers {
        var i = 0
        var clockZero = true
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
                        try {
                            instructions[i + registers.valueOf(instruction.arg1)!!].tgl()
                        } catch (e: Exception) { }

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

    val instructions = input
        .groupValues(pattern)
        .map { Assembunny(it[0], it[1], it[2]) }

    val p1Registers = Registers()
    val p2Registers = Registers().apply { updateValue("c", 1) }

    fun part1() = runInstructions(instructions, p1Registers).a

    fun part2() = runInstructions(instructions, p2Registers).a
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D12.part1()} (${elapsedTime(time)}ms)") // 318117
    time = System.nanoTime()
    println("Part 2: ${Y2016D12.part2()} (${elapsedTime(time)}ms)") // 9227771
}