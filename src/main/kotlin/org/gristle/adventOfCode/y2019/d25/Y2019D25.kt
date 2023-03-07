package org.gristle.adventOfCode.y2019.d25

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2019.IntCode.ICSave
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.io.IOException
import java.util.*

class Y2019D25(private val input: String) : Day {

    fun play() {
        val initialState = input.split(',').map { it.toLong() }
        val output: Deque<Long> = LinkedList()
        val toComp: Deque<Long> = LinkedList()
        val intCode = IntCode("A", initialState, null, toComp, output)
        intCode.run()
        val saves = mutableMapOf<String, ICSave>()
        while (true) {
            output.print()
            val input = readLine() ?: throw IOException("Failed to read from console")

            if (input.contains("save ")) {
                saves[input.drop(5)] = intCode.save()
                continue
            }

            if (input.contains("load ")) {
                intCode.restore(saves.getValue(input.drop(5)))
                continue
            }

            toComp.addAll(input.map { it.code.toLong() })
            toComp.add(10L)
            intCode.run()
        }
    }

    private fun Deque<Long>.print() {
        val output = buildString {
            this@print.forEach {
                append(it.toInt().toChar())
            }
        }
        println(output)
        clear()
    }

    override fun part1() = "need to write a player"

    override fun part2() = "Merry Xmas!!!"
}

fun main() {
    Y2019D25(readRawInput("y2019/d25")).play()
}
