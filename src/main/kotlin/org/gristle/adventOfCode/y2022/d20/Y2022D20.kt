package org.gristle.adventOfCode.y2022.d20

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getLongList

class Y2022D20(input: String) : Day {

    private val values = input.getLongList()

    private fun solve(factor: Int, times: Int): Long {

        // Pair numbers with their original index to ensure numbers are unique. I don't know a better way of getting
        // the numbers in order other than doing an indexOf function.
        val numbers = MutableList(values.size) { index -> IndexedValue(index, values[index] * factor) }

        repeat(times) { // repeat swapping process n times

            numbers.indices.forEach { currentIndex -> // swap for each number in the original order
                val nextIndex = numbers.indexOfFirst { it.index == currentIndex }
                val number = numbers.removeAt(nextIndex)
                val addIndex = (nextIndex + number.value).mod(numbers.size)
                numbers.add(addIndex, number)
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

fun main() = Day.runDay(Y2022D20::class)

//    Class creation: 19ms
//    Part 1: 4151 (48ms) (original 216ms)
//    Part 2: 7848878698663 (435ms) (original 1006ms)
//    Total time: 503ms