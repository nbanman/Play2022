package org.gristle.adventOfCode.y2017.d18

import org.gristle.adventOfCode.utilities.*
import java.util.*

// Not refactored. Ugly but fast enough.
object Y2017D18 {
    private val input = readRawInput("y2017/d18")

    private val pattern = """([a-z]{3}) ([-a-z0-9]+)(?: ([-a-z0-9]+))?""".toRegex()

    data class Command1718(val name: String, val arg1: String, val arg2: String)

    data class Program(
        val commands: List<Command1718>,
        val p: Long,
        val ownDeque: Deque<Long>,
        val otherDeque: Deque<Long>
    ) {
        var index = 0
        var deadlock = false
        var sends = 0
        private val reg = mutableMapOf<String, Long>("p" to p)

        fun valueOf(arg: String) = if (arg.last().isDigit()) {
            arg.toLong()
        } else {
            reg[arg] ?: 0
        }

        fun execute() {
            if (index !in commands.indices) {
                deadlock = true
                return
            }
            val command = commands[index]
            if (deadlock) {
                rcv(command)
            } else {
                when (command.name) {
                    "snd" -> {
                        otherDeque.add(valueOf(command.arg1))
                        sends++
                    }

                    "set" -> {
                        reg[command.arg1] = valueOf(command.arg2)
                    }
                    "add" -> {
                        reg[command.arg1] = valueOf(command.arg1) + valueOf(command.arg2)
                    }
                    "mul" -> {
                        reg[command.arg1] = valueOf(command.arg1) * valueOf(command.arg2)
                    }
                    "mod" -> {
                        reg[command.arg1] = valueOf(command.arg1) % valueOf(command.arg2)
                    }
                    "rcv" -> {
                        rcv(command)
                        return
                    }
                    "jgz" -> {
                        if (valueOf(command.arg1) > 0) {
                            index += this.valueOf(command.arg2).toInt()
                            return
                        }
                    }
                }
                index++
            }
        }

        private fun rcv(command: Command1718) {
            if(ownDeque.isNotEmpty()) {
                reg[command.arg1] = ownDeque.pop()
                index++
                deadlock = false
            } else {
                deadlock = true
            }
        }
    }

    private val commands = input
        .groupValues(pattern)
        .map { Command1718(it[0], it[1], it[2]) }

    fun part1(): Long {
        val registers = mutableMapOf<String, Long>()
        var frequency = 0L
        var recover = 0L

        fun valueOf(arg: String) = if (arg.last().isDigit()) {
            arg.toLong()
        } else {
            registers[arg] ?: 0
        }

        var i = 0
        while (i in commands.indices) {
            val command = commands[i]
            when (command.name) {
                "snd" -> { frequency = valueOf(command.arg1) }
                "set" -> {
                    registers[command.arg1] = valueOf(command.arg2)
                }
                "add" -> {
                    registers[command.arg1] = valueOf(command.arg1) + valueOf(command.arg2)
                }
                "mul" -> {
                    registers[command.arg1] = valueOf(command.arg1) * valueOf(command.arg2)
                }
                "mod" -> {
                    registers[command.arg1] = valueOf(command.arg1) % valueOf(command.arg2)
                }
                "rcv" -> {
                    if (valueOf(command.arg1) != 0L) {
                        recover = frequency
                        return recover
                    }

                }
                "jgz" -> {
                    if (valueOf(command.arg1) > 0) {
                        i += valueOf(command.arg2).toInt()
                        continue
                    }
                }
            }
            i++
        }
        return -1
    }

    fun part2(): Int {
        val dequeA: Deque<Long> = LinkedList()
        val dequeB: Deque<Long> = LinkedList()

        val programA = Program(commands, 0L, dequeA, dequeB)
        val programB = Program(commands, 1L, dequeB, dequeA)

        while(!(programA.deadlock && programB.deadlock)) {
            programA.execute()
            programB.execute()
        }
        return programB.sends
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D18.part1()} (${elapsedTime(time)}ms)") // 9423
    time = System.nanoTime()
    println("Part 2: ${Y2017D18.part2()} (${elapsedTime(time)}ms)") // 7620
}