package org.gristle.adventOfCode.y2022.d3

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D3(input: String) {

    private val rucksacks = input.lines()

    private fun Char.priority() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27

    // for each character, obtain priority n and then place a '1' bit n to the left of 1.
    private fun CharSequence.toBitSet() = fold(0L) { acc, c -> acc or (1L shl c.priority()) }

    fun part1() = rucksacks.sumOf { sack ->
        sack
            .chunked(sack.length / 2) { it.toBitSet() } // splits each sack into two halves turn each half into a bitset
            .let { (a, b) -> (a and b).countTrailingZeroBits() } // find common bit and convert to priority
    }

    fun part2() = rucksacks
        .map { it.toBitSet() } // turn each string into a bitset
        .chunked(3) // chunk in groups of three
        .sumOf { it.reduce(Long::and).countTrailingZeroBits() } // find common bit and convert to priority
}

fun main() {
    val timer = Stopwatch(start = true)
    val solver = Y2022D3(getInput(3, 2022))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 7428 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 2650
    println("Total time: ${timer.elapsed()}ms")
}