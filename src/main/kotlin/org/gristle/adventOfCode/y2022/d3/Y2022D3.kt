package org.gristle.adventOfCode.y2022.d3

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D3(input: String) {

    private val rucksacks = input.lines()

    private fun Char.priority() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27

    fun part1() = rucksacks.sumOf { sack ->
        sack
            .chunked(sack.length / 2) // splits each sack into two halves
            .map(String::toSet) // turn each half into a set
            .let { (first, second) -> first.intersect(second).first() } // get the Char that's in both sets
            .priority() // get the priority of that char
    }

    fun part2() = rucksacks
        .map(String::toSet) // turn each string into a Set<Char>
        .chunked(3) // chunk in groups of three
        .sumOf { chunk ->  // sum each chunk by the priority of the char shared by all three
            chunk.reduce(Set<Char>::intersect) // get char shared by all three
                .first()
                .priority() // get priority
        }
}

fun main() {
    val timer = Stopwatch(start = true)
    val solver = Y2022D3(getInput(3, 2022))
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 7428
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 2650
    println("Total time: ${timer.elapsed()}ms")
}