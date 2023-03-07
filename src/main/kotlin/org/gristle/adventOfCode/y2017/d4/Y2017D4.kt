package org.gristle.adventOfCode.y2017.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.eachCount

class Y2017D4(input: String) : Day {

    private val passphrases = input.lines().map { it.split(' ') }

    private fun <T> List<List<T>>.countUnique() = count { it.size == it.distinct().size }

    override fun part1() = passphrases.countUnique()

    override fun part2() = passphrases
        .map { phrase -> phrase.map { word -> word.eachCount() } } // convert words into letter distributions
        .countUnique()
}

fun main() = Day.runDay(Y2017D4::class)

//    Class creation: 24ms
//    Part 1: 455 (2ms)
//    Part 2: 186 (13ms)
//    Total time: 40ms
