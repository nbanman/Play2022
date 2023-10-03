package org.gristle.adventOfCode.y2022.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.poll
import org.gristle.adventOfCode.utilities.toPriorityQueueDescending

class Y2022D1(input: String) : Day {
    private val calories: List<Int> = input
        .blankSplit() // group each elf's snacks
        // sum all the ints found in each elf's string, then put them all in a descending priority queue
        .toPriorityQueueDescending { it.getInts().sum() }
        .poll(3) // grab the top three contenders

    override fun part1(): Int = calories.first() // peek at the top of the priority queue, which holds the highest sum
    override fun part2(): Int = calories.sum() // sum the top three contenders
}

fun main() = Day.runDay(Y2022D1::class) // 71300, 209691

//    Class creation: 26ms
//    Part 1: 71300 (0ms)
//    Part 2: 209691 (0ms)
//    Total time: 26ms