package org.gristle.adventOfCode.y2019.d5

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import org.gristle.adventOfCode.y2019.d2.Y2019D2
import java.util.*

object Y2019D5 {
    private val code = readRawInput("y2019/d5")

    private val input: Deque<Long> = LinkedList()
    private val output: Deque<Long> = LinkedList()

    val comp = IntCode(
        "codey",
        code.split(',').map { it.toLong() },
        input = input,
        output = output
    )


    fun part1(): Long {
        input.add(1)
        comp.run()
        return output.last
    }

    fun part2(): Long {
        comp.reset()
        input.add(5)
        comp.run()
        return output.last
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D5.part1()} (${elapsedTime(time)}ms)") // 7839346
    time = System.nanoTime()
    println("Part 2: ${Y2019D5.part2()} (${elapsedTime(time)}ms)") // 447803 
}