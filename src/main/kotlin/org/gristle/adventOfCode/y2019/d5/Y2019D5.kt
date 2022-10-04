package org.gristle.adventOfCode.y2019.d5

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

class Y2019D5(input: String) {

    private val intCodeInput: Deque<Long> = LinkedList()
    private val output: Deque<Long> = LinkedList()

    private val comp = IntCode(
        "codey",
        input.split(',').map { it.toLong() },
        input = intCodeInput,
        output = output
    )

    fun part1(): Long {
        intCodeInput.add(1)
        comp.run()
        return output.last
    }

    fun part2(): Long {
        comp.reset()
        intCodeInput.add(5)
        comp.run()
        return output.last
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D5(readRawInput("y2019/d5"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 7839346
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 447803
}