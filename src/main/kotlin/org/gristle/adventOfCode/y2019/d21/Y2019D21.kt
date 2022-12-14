package org.gristle.adventOfCode.y2019.d21

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toMutableGrid
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

class Y2019D21(private val input: String) {

    fun execute(commands: String): Long {
        val initialState = input.split(',').map { it.toLong() }
        val toDroid: Deque<Long> = LinkedList()
        val toComp: Deque<Long> = LinkedList()
        val intCode = IntCode("A", initialState, null, toComp, toDroid)
        intCode.run()
        commands.split("\n").forEach {
            addCommand(it, toComp)
            intCode.run()
        }
        return toDroid.last()
    }


    private fun addCommand(command: String, toComp: Deque<Long>) {
        val commands = command.map { it.code.toLong() }
        toComp.addAll(commands)
        toComp.add('\n'.code.toLong())
    }

    @Suppress("unused")
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
    val c = Y2019D21(readRawInput("y2019/d21"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 19349530
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1142805439
}