package org.gristle.adventOfCode.y2019.d21

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

object Y2019D21 {
    private val input = readRawInput("y2019/d21")

    fun execute(commands: String): Long {
        val initialState = input.split(',').map { it.toLong() }
        val toDroid: Deque<Long> = LinkedList<Long>()
        val toComp: Deque<Long> = LinkedList<Long>()
        val intCode = IntCode("A", initialState, null, toComp, toDroid)
        intCode.run()
        commands.split("\n").forEach {
            addCommand(it, toComp)
            intCode.run()
        }
        return toDroid.last()
    }

    private fun addCommand(command: String, toComp: Deque<Long>) {
        val commands = command.map { it.toLong() }
        toComp.addAll(commands)
        toComp.add('\n'.code.toLong())
    }

    private fun printOutput(toDroid: Deque<Long>) {
        val width = toDroid.indexOfFirst { it == 10L }
        val grid = toDroid.mapNotNull { if (it != null && it != 10L) it else null }.toMutableGrid(width)
        println(grid.representation { it.toInt().toChar() })
    }

    fun part1(): Long {
        val commands = """NOT A T
NOT C J
OR T J
AND D J
WALK"""
        return execute(commands)
    }

    fun part2(): Long {
        val commands = """OR A J
AND B J  
AND C J  
NOT J J  
AND D J  
OR E T  
OR H T  
AND T J  
RUN"""
        return execute(commands)
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D21.part1()} (${elapsedTime(time)}ms)") // 19349530
    time = System.nanoTime()
    println("Part 2: ${Y2019D21.part2()} (${elapsedTime(time)}ms)") // 1142805439
}