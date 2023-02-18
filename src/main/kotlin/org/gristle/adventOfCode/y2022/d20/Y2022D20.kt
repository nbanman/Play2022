package org.gristle.adventOfCode.y2022.d20

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getLongs

class Y2022D20(private val input: String) : Day {

    fun solve(factor: Int, times: Int): Long {
        // Pair numbers with their original index to ensure numbers are unique. I don't know a better way of getting
        // the numbers in order other than doing an indexOf function.
        val numbers = input.getLongs().map { it * factor }.withIndex().toMutableList()

        repeat(times) { // repeat swapping process n times
            numbers.indices.forEach { currentIndex -> // swap for each number in the original order
                val nextIndex = numbers.indexOfFirst { it.index == currentIndex }
                val number = numbers.removeAt(nextIndex)
                numbers.add((nextIndex + number.value).mod(numbers.size), number)
            }
        }
        val zeroIndex = numbers.indexOfFirst { it.value == 0L }
        return (1..3).fold(0L) { acc, thousand ->
            acc + numbers[(zeroIndex + thousand * 1000) % numbers.size].value
        }
    }

    override fun part1() = solve(1, 1)

    override fun part2() = solve(811589153, 10)
}

// pt 1: 4151 (87ms) (original 216ms) 
// pt 2: 7848878698663 (488ms) (original 1006ms)
fun main() = Day.runDay(20, 2022, Y2022D20::class)