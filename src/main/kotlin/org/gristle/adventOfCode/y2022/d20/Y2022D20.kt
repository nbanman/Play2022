package org.gristle.adventOfCode.y2022.d20

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.getInts
import kotlin.math.abs
import kotlin.math.sign

class Y2022D20(input: String) {

    private val numbers = input.getInts().map { it.toLong() }.withIndex().toList()

    fun <E> MutableList<E>.swap(index: Int, amount: Long) {
        tailrec fun <E> swap(mList: MutableList<E>, index: Int, times: Long, direction: Int) {
            if (times == 0L) return
            val temp = mList[index]
            val nextIndex = (index + direction).mod(mList.size)
            mList[index] = mList[nextIndex]
            mList[nextIndex] = temp
            swap(mList, nextIndex, times - 1, direction)
        }
        swap(this, index, abs(amount) % (size - 1), amount.sign)
    }

    fun part1() = run {
        val move = numbers.toMutableList()
        var zeroIndex: IndexedValue<Long>? = null
        numbers.forEach { number ->
            if (number.value == 0L) zeroIndex = number
            move.swap(move.indexOf(number), number.value)
        }
        (1..3).fold(0L) { acc, l ->
            acc + move[(move.indexOf(zeroIndex) + l * 1000).mod(move.size)].value
        }
    }

    fun part2() = run {
        val numbers = numbers.map { (index, value) -> IndexedValue(index, value * 811589153L) }
        val move = numbers.toMutableList()
        var zeroIndex: IndexedValue<Long>? = null
        repeat(10) {
            numbers.forEach { number ->
                if (number.value == 0L) zeroIndex = number
                move.swap(move.indexOf(number), number.value)
            }
        }
        (1..3).fold(0L) { acc, l ->
            acc + move[(move.indexOf(zeroIndex) + l * 1000).mod(move.size)].value
        }

    }
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
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 4151
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}