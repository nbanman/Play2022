package org.gristle.adventOfCode.y2015.d23

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues

class Y2015D23(input: String) : Day {

    data class Instruction(val name: String, val register: String, val offset: Int) {
        companion object {
            fun of(name: String, register: String, offset: String): Instruction {
                val jump = if (offset == "") 0 else offset.toInt()
                return Instruction(name, register, jump)
            }
        }
    }

    private val pattern = """([a-z]{3}) ([ab])?(?:(?:, )?([-+]\d+))?""".toRegex()
    private val instructions = input
        .groupValues(pattern)
        .map { (name, register, offset) -> Instruction.of(name, register, offset) }

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

    override fun part1() = solve()

    override fun part2() = solve(1)
}

fun main() = Day.runDay(Y2015D23::class)

//    Class creation: 19ms
//    Part 1: 255 (0ms)
//    Part 2: 334 (0ms)
//    Total time: 20ms