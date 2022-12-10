package org.gristle.adventOfCode.y2022.d10

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.ocr
import org.gristle.adventOfCode.utilities.toMutableGrid

class Y2022D10(input: String) {

    // add an extra "a " in front of input because that will add an extra 0 to the eventual List<Int> we are making
    // parts 1 and 2 both care about what the register is mid-cycle, before the register is updated. Thus, an extra
    // 0 needs to be put in so that the relevant register value is paired with the cycle.

    // The split/map does not look at commands. It grabs all non-whitespace "words," converts numbers into numbers,
    // and non-numbers into zeros. By doing this, we map the number of cycles where the register does not change.
    private val cpu = "a $input"
        .split('\n', ' ')
        .map { it.toIntOrNull() ?: 0 }
        // keeps a running tally of where the register is, starting from 1.
        .runningFold(1, Int::plus) // register state per cycle
        // keeps track of which cycle the cpu is in. Useful because the functions drop and filter, but we want to keep
        // the register paired with the cycle.
        .withIndex()
        .take(241) // The OCR grid needs 240 exact, and we'll be dropping the first value.
        .drop(1) // Having used the extra entry to move the registers back by one, we can now drop that dummy entry

    fun part1() = cpu
        .filter { (cycle, _) -> (cycle + 20) % 40 == 0 }
        .sumOf { (cycle, register) -> cycle * register }

    fun part2() = cpu
        .map { (cycle, register) -> ((cycle - 1) % 40) in (register - 1)..(register + 1) }
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