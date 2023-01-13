package org.gristle.adventOfCode.y2022.d20

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.getLongs

class Y2022D20(private val input: String) {

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

    fun part1() = solve(1, 1)

    fun part2() = solve(811589153, 10)
}

fun main() {
    val input = getInput(20, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D20(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 4151 (87ms) (original 216ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 7848878698663 (488ms) (original 1006ms)
    println("Total time: ${timer.elapsed()}ms")
}