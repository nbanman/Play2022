package org.gristle.adventOfCode.y2017.d23

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2017.d18.Y2017D18

// not refactored! ugly!
class Y2017D23(input: String) {

    private val pattern = """(\w{3}) (-?\w+) (-?\w+)""".toRegex()
    
    val commands = input
        .groupValues(pattern)
        .map { Y2017D18.Command(it[0], it[1], it[2]) }

    private var registers = mutableMapOf<String, Long>()

    private fun valueOf(arg: String) = if (arg.last().isDigit()) {
        arg.toLong()
    } else {
        registers[arg] ?: 0L
    }

    fun part1(): Int {
        var index = 0
        var p1 = 0
        while (index in commands.indices) {
            val command = commands[index]
            when (command.name) {
                "set" -> {
                    registers[command.arg1] = valueOf(command.arg2)
                }
                "sub" -> {
                    registers[command.arg1] = valueOf(command.arg1) - valueOf(command.arg2)
                }
                "mul" -> {
                    p1++
                    registers[command.arg1] = valueOf(command.arg1) - valueOf(command.arg2)
                }
                "jnz" -> {
                    if (valueOf(command.arg1) != 0L) {
                        index += valueOf(command.arg2).toInt()
                        continue
                    }
                }
            }
            index++
        }
        return p1
    }

    fun part2(): Int {
        // Part 2
        val b = commands.first().arg2.toInt() * 100 + 100_000
        return (b..b + 17_000 step 17).count {
            !it.toBigInteger().isProbablePrime(2)
        }
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D23(readRawInput("y2017/d23"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 3025
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 915
}