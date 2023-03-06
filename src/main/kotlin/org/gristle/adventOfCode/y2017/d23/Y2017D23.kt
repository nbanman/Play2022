package org.gristle.adventOfCode.y2017.d23

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.y2017.d18.Y2017D18

// not refactored! ugly!
class Y2017D23(input: String) : Day {

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

    override fun part1(): Int {
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

    override fun part2(): Int {
        // Part 2
        val b = commands.first().arg2.toInt() * 100 + 100_000
        return (b..b + 17_000 step 17).count {
            !it.toBigInteger().isProbablePrime(2)
        }
    }
}

fun main() = Day.runDay(23, 2017, Y2017D23::class)

//    Class creation: 20ms
//    Part 1: 3025 (12ms)
//    Part 2: 915 (15ms)
//    Total time: 47ms