package org.gristle.adventOfCode.y2022.d10

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.ocr
import org.gristle.adventOfCode.utilities.toMutableGrid

class Y2022D10(input: String) {

    // Parsing is a little unconventional. Rather than reading each line as a command and figuring out what each 
    // command does, we simply want to arrive at a list of cycles paired to their respective registry value. We can
    // start by splitting the string into "words" of non-whitespace characters, attempting to convert them all to
    // Ints, and substituting a 0 when conversion fails because the word is "addx" or "noop." This returns a list of
    // adjustments to the registry that occur at the end of a particular cycle.
    // 
    // The next step is to do a runningFold with a plus function on the list, which returns the list of registry 
    // values at the end of each cycle. We add the cycle information with .withIndex(), which allows us to filter
    // unused registry values without losing track of what cycle we are in.
    private val cpu = input
        .split('\n', ' ') // split into "words"
        .map { it.toIntOrNull() ?: 0 } // map each word to an Int, or if that fails to 0
        .runningFold(1, Int::plus) // register state per cycle
        .withIndex() // keeps track of which cycle the cpu is in.

    // Note that for both part 1 and part 2, our List gives the registry value *after* a particular cycle is completed.
    // The registry value is updated at the *end* of the cycle, which means we want to use the registry value of the
    // *previous* cycle rather than the cycle mentioned in the instructions. So both evaluations start at cycle 0 
    // (initial state) rather than cycle 1, and perform each evaluation one cycle early.
    fun part1() = cpu
        .filter { (cycle, _) -> (cycle + 19) % 40 == 0 }
        .sumOf { (cycle, register) -> (cycle + 1) * register }

    fun part2() = cpu
        .take(240) // The OCR grid needs 240 exact
        .map { (cycle, register) -> ((cycle) % 40) in (register - 1)..(register + 1) }
        .toMutableGrid(40)
        // .apply { println("\n${rep()}") }
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