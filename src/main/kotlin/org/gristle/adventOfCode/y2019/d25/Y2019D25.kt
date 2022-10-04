package org.gristle.adventOfCode.y2019.d25

import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2019.IntCode.ICSave
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

class Y2019D25(private val input: String) {

    fun play() {
        val initialState = input.split(',').map { it.toLong() }
        val output: Deque<Long> = LinkedList()
        val toComp: Deque<Long> = LinkedList()
        val intCode = IntCode("A", initialState, null, toComp, output)
        intCode.run()
        val saves = mutableMapOf<String, ICSave>()
        while (true) {
            output.print()
            val input = readLine()

            if (input!!.contains("save ")) {
                saves[input.drop(5)] = intCode.save()
                continue
            }

            if (input.contains("load ")) {
                intCode.restore(saves[input.drop(5)]!!)
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
}

fun main() {
    Y2019D25(readRawInput("y2019/d25")).play() 
}
