package org.gristle.adventOfCode.y2019.d2

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

object Y2019D2 {
    private val input = readRawInput("y2019/d2")

    val output: Deque<Long> = LinkedList()

    val comp = IntCode(
        "codey",
        input.split(',').map { it.toLong() },
        output = output
    )

    fun part1(): Long {
        comp.program[1] = 12
        comp.program[2] = 2
        comp.run()
        return comp.program[0]!!
    }

    fun part2(): Long {
        for (noun in 0L..99) for (verb in 0L..99) {
            comp.reset()
            comp.program[1] = noun
            comp.program[2] = verb
            comp.run()
            if (comp.program[0] == 19690720L) return 100 * noun + verb
        }
        return -1L
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D2.part1()} (${elapsedTime(time)}ms)") // 3895705
    time = System.nanoTime()
    println("Part 2: ${Y2019D2.part2()} (${elapsedTime(time)}ms)") // 6417
}