package org.gristle.adventOfCode.y2016.d12

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.y2016.shared.Assembunny
import org.gristle.adventOfCode.y2016.shared.Registers
import org.gristle.adventOfCode.y2016.shared.runInstructions

class Y2016D12(input: String) : Day {

    val instructions = let {
        val pattern = """(\w+) (-?\w+)(?: (-?\w+))?""".toRegex()
        input
            .groupValues(pattern)
            .map { Assembunny(it[0], it[1], it[2]) }
    }

    private val p1Registers = Registers()
    private val p2Registers = Registers().apply { updateValue("c", 1) }

    override fun part1() = runInstructions(instructions, p1Registers).a

    override fun part2() = runInstructions(instructions, p2Registers).a
}

fun main() = Day.runDay(Y2016D12::class)

//Class creation: 15ms
//Part 1: 318117 (64ms)
//Part 2: 9227771 (450ms)
//Total time: 530ms