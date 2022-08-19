package org.gristle.adventOfCode.y2016.d23

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2016.d12.Assembunny
import org.gristle.adventOfCode.y2016.d12.Registers
import org.gristle.adventOfCode.y2016.d12.Y2016D12.runInstructions

object Y2016D23 {
    private val input = readRawInput("y2016/d23")

    private val pattern = """(\w+) (-?\w+)(?: (-?\w+))?""".toRegex()

    val instructions = input
        .groupValues(pattern)
        .map { Assembunny(it[0], it[1], it[2]) }

    private val p1Registers = Registers().apply { updateValue("a", 7) }

    fun part1() = runInstructions(instructions, p1Registers).a

    fun part2() = (1..12).reduce { acc, i -> acc * i } + 94 * 82
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D23.part1()} (${elapsedTime(time)}ms)") // 12748
    time = System.nanoTime()
    println("Part 2: ${Y2016D23.part2()} (${elapsedTime(time)}ms)") // 479009308
}