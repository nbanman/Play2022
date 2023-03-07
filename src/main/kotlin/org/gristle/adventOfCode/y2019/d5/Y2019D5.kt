package org.gristle.adventOfCode.y2019.d5

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

class Y2019D5(input: String) : Day {

    private val intCodeInput: Deque<Long> = LinkedList()
    private val output: Deque<Long> = LinkedList()

    private val comp = IntCode(
        "codey",
        input.split(',').map { it.toLong() },
        input = intCodeInput,
        output = output
    )

    override fun part1(): Long {
        intCodeInput.add(1)
        comp.run()
        return output.last
    }

    override fun part2(): Long {
        comp.reset()
        intCodeInput.add(5)
        comp.run()
        return output.last
    }
}

fun main() = Day.runDay(Y2019D5::class)

//    Class creation: 19ms
//    Part 1: 7839346 (0ms)
//    Part 2: 447803 (0ms)
//    Total time: 20ms