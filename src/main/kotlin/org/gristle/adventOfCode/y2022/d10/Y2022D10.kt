package org.gristle.adventOfCode.y2022.d10

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.ocr
import org.gristle.adventOfCode.utilities.toMutableGrid

class Y2022D10(input: String) {

    private val cpu = input
        .split('\n', ' ')
        .map { it.toIntOrNull() ?: 0 }
        .runningFold(1, Int::plus) // register state per cycle
        .withIndex()
        .take(240)

    fun part1() = cpu
        .filter { (cycle, _) -> (cycle + 21) % 40 == 0 }
        .sumOf { (cycle, register) -> (cycle + 1) * register }

    fun part2() = cpu
        .map { (cycle, register) -> (cycle % 40) in (register - 1)..(register + 1) }
        .toMutableGrid(40)
        .ocr()
}

fun main() {
    val input = getInput(10, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D10(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 12840
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // ZKJFBJFZ
    println("Total time: ${timer.elapsed()}ms")
}