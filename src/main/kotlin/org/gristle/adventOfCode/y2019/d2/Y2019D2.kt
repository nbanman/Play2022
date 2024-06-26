package org.gristle.adventOfCode.y2019.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2019.ic.IntCode

import java.util.*

class Y2019D2(input: String) : Day {

    private val output: Deque<Long> = LinkedList()

    private val comp = IntCode(
        "codey",
        input.split(',').map { it.toLong() },
        output = output
    )

    override fun part1(): Long {
        comp.program[1] = 12
        comp.program[2] = 2
        comp.run()
        return comp.program.getValue(0)
    }

    override fun part2(): Long {
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

fun main() = Day.runDay(Y2019D2::class)

//    Class creation: 18ms
//    Part 1: 3895705 (0ms)
//    Part 2: 6417 (99ms)
//    Total time: 117ms