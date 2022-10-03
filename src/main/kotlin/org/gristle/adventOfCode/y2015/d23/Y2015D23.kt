package org.gristle.adventOfCode.y2015.d23

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D23(input: String) {

    data class Instruction(val name: String, val register: String, val offset: Int) {
        companion object {
            fun of(name: String, register: String, offset: String): Instruction {
                val jump = if (offset == "") 0 else offset.toInt()
                return Instruction(name, register, jump)
            }
        }
    }

    private val pattern = """(hlf|tpl|inc|jmp|jie|jio) ([ab])?(?:(?:, )?([-+]\d+))?""".toRegex()
    private val instructions = input
        .groupValues(pattern)
        .map { Instruction.of(it[0], it[1], it[2]) }

    fun solve(aStart: Int = 0): Int {
        var a = aStart
        var b = 0
        var index = 0
        while (index < instructions.size) {
            val instruction = instructions[index]
            when (instruction.name) {
                "hlf" -> {
                    if (instruction.register == "a") a /= 2 else b /= 2
                    index++
                }
                "tpl" -> {
                    if (instruction.register == "a") a *= 3 else b *= 3
                    index++
                }
                "inc" -> {
                    if (instruction.register == "a") a++ else b++
                    index++
                }
                "jmp" -> index += instruction.offset
                "jie" -> if (instruction.register == "a") {
                    if (a % 2 == 0) index += instruction.offset else index++
                } else {
                    if (b % 2 == 0) index += instruction.offset else index++
                }
                "jio" -> if (instruction.register == "a") {
                    if (a == 1) index += instruction.offset else index++
                } else {
                    if (b == 1) index += instruction.offset else index++
                }
            }
        }
        return b
    }

    fun part1() = solve()

    fun part2() = solve(1)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D23(readRawInput("y2015/d23"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 255
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 334
}