package org.gristle.adventOfCode.y2022.d20

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.getLongs
import kotlin.math.abs
import kotlin.math.sign

class Y2022D20(private val input: String) {

    // Swapping operation. Experimented with holding on to final swap until last operation, but it was slower.
    private fun <E> MutableList<E>.swap(index: Int, amount: Long) {
        tailrec fun <E> swap(mList: MutableList<E>, index: Int, times: Long, direction: Int) {
            if (times == 0L) return
            val temp = mList[index]
            val nextIndex = (index + direction).mod(mList.size)
            mList[index] = mList[nextIndex]
            mList[nextIndex] = temp
            swap(mList, nextIndex, times - 1, direction)
        }
        // Optimization: after swapping listSize - 1, the list order is the same. We don't care about list order
        // since we are constantly doing indexOf searches.
        swap(this, index, abs(amount) % (size - 1), amount.sign)
    }

    fun solve(factor: Int, times: Int): Long {
        // Pair numbers with their original index to ensure numbers are unique. I don't know a better way of getting
        // the numbers in order other than doing an indexOf function.
        val numbers = input.getLongs().map { it * factor }.withIndex()

        // Create the list to swap numbers with
        val move = numbers.toMutableList()

        repeat(times) { // repeat swapping process n times
            numbers.forEach { number -> // swap for each number in the original order
                move.swap(move.indexOf(number), number.value) // indexOf call is slow, but c'est la vie
            }
        }
        val zeroIndex = numbers.find { it.value == 0L }
        return (1..3).fold(0L) { acc, thousand ->
            acc + move[(move.indexOf(zeroIndex) + thousand * 1000).mod(move.size)].value
        }
    }

    fun part1() = solve(1, 1)

    fun part2() = solve(811589153, 10)
}

fun main() {
    val input = listOf(
        getInput(20, 2022),
        """1
2
-3
3
-2
0
4""",
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D20(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 4151 (216ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 7848878698663 (1006ms)
    println("Total time: ${timer.elapsed()}ms")
}