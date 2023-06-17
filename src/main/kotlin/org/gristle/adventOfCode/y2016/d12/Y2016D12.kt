package org.gristle.adventOfCode.y2016.d12

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.y2016.shared.Assembunny

class Y2016D12(input: String) : Day {
    private val instructions = Assembunny.parseInstructions(input)
    private val assembunny = Assembunny()

    override fun part1(): Int {
        assembunny.reset()
        return assembunny.runInstructions(instructions)['a']
    }

    override fun part2(): Int {
        assembunny.reset()['c'] = 1
        return assembunny.runInstructions(instructions)['a']
    }
}

fun main() = Day.runDay(Y2016D12::class)

//    Class creation: 5ms
//    Part 1: 318117 (61ms)
//    Part 2: 9227771 (433ms)
//    Total time: 499ms