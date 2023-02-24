package org.gristle.adventOfCode.y2016.d23

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.y2016.shared.Assembunny
import org.gristle.adventOfCode.y2016.shared.Registers
import org.gristle.adventOfCode.y2016.shared.runInstructions

class Y2016D23(input: String) : Day {

    private val pattern = """(\w+) (-?\w+)(?: (-?\w+))?""".toRegex()

    private val instructions = input
        .groupValues(pattern)
        .map { Assembunny(it[0], it[1], it[2]) }

    private val p1Registers = Registers().apply { updateValue("a", 7) }

    override fun part1() = runInstructions(instructions, p1Registers).a

    override fun part2() = (1..12).reduce { acc, i -> acc * i } + 94 * 82
}

fun main() = Day.runDay(23, 2016, Y2016D23::class)

//Class creation: 19ms
//Part 1: 12748 (19ms)
//Part 2: 479009308 (0ms)
//Total time: 38ms