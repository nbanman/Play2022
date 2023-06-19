package org.gristle.adventOfCode.y2017.d1

import org.gristle.adventOfCode.Day

class Y2017D1(input: String) : Day {
    private val numbers = input.map(Char::digitToInt)

    private inline fun solve(comparisonIndex: List<Int>.(index: Int) -> Int): Int = numbers
        .filterIndexed { index, i -> numbers[numbers.comparisonIndex(index)] == i }
        .sum()

    override fun part1() = solve { (it + 1) % size }

    override fun part2() = solve { (it + size / 2) % size }
}

fun main() = Day.runDay(Y2017D1::class)

//    Class creation: 20ms
//    Part 1: 1182 (1ms)
//    Part 2: 1152 (0ms)
//    Total time: 22ms