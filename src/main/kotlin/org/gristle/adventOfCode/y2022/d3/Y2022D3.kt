package org.gristle.adventOfCode.y2022.d3

import org.gristle.adventOfCode.Day

class Y2022D3(input: String) : Day {
    private val rucksacks = input.lineSequence()

    private fun Char.priority() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27

    // for each character, obtain priority n and then place a '1' bit n to the left of 1.
    private fun CharSequence.toBitSet() = fold(0L) { acc, c -> acc or (1L shl c.priority()) }

    override fun part1() = rucksacks.sumOf { sack ->
        sack
            // splits each sack into two halves and turns each half into a bitset
            .chunked(sack.length / 2) { it.toBitSet() }
            // find common bit and convert to priority
            .let { (a, b) -> (a and b).countTrailingZeroBits() }
    }

    override fun part2() = rucksacks
        .map { it.toBitSet() } // turn each string into a bitset
        .chunked(3) // chunk in groups of three
        .sumOf { it.reduce(Long::and).countTrailingZeroBits() } // find common bit and convert to priority
}

fun main() = Day.runDay(3, 2022, Y2022D3::class) // 7428, 2650