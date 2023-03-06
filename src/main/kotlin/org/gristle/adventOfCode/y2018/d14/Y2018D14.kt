package org.gristle.adventOfCode.y2018.d14

import org.gristle.adventOfCode.Day

class Y2018D14(input: String) : Day {
    private val inputNum = input.toInt()

    private val solution: Pair<String, Int> = let {
        var size = 2
        val recipes = MutableList(30_000_000) { -1 }
        recipes[0] = 3
        recipes[1] = 7
        var elves = listOf(0, 1)
        while (recipes.subList(maxOf(size - 7, 0), size - 1).joinToString("")
                .toInt() != inputNum && size < recipes.size
        ) {
            val (ten, one) = elves.sumOf { recipes[it] }.let { it / 10 to it % 10 }
            if (ten != 0) {
                recipes[size] = ten
                size++
            }
            recipes[size] = one
            size++

            elves = elves.map { elf ->
                (elf + (recipes[elf] + 1)) % size
            }
        }
        recipes.subList(inputNum, inputNum + 10).joinToString("") to size - 7
    }

    override fun part1() = solution.first
    override fun part2() = solution.second
}

fun main() = Day.runDay(Y2018D14::class)

//    Class creation: 5844ms
//    Part 1: 4910101614 (0ms)
//    Part 2: 20253137 (0ms)
//    Total time: 5844ms