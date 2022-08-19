package org.gristle.adventOfCode.y2019.d16

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.abs

object Y2019D16 {
    private val input = readRawInput("y2019/d16")
    private val nos = input.map { Character.getNumericValue(it) }
    private const val PHASES = 100

    fun part1(): String {

        val startPattern = listOf(0, 1, 0, -1)
        return (1..PHASES).fold(nos) { acc, _ ->
            List(acc.size) { index ->
                acc.foldIndexed(0) { index2, acc2, ii ->
                    val ai = ((index2 + 1) / (index + 1)) % 4
                    acc2 + ii * startPattern[ai]
                }.let { abs(it % 10) }
            }
        }.take(8)
        .joinToString("")
    }

    fun part2(): String {
        val offset = input.take(7).toInt()
        val arraySize = nos.size * 10_000 - offset
        val offsetMod = offset % nos.size
        val nosi = IntArray(arraySize) { i -> nos[(offsetMod + i) % nos.size] }
        repeat(PHASES) {
            for (i in nosi.lastIndex - 1 downTo 0) {
                nosi[i] = (nosi[i] + nosi[i + 1]) % 10
            }
        }
        return nosi.take(8).joinToString("")
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D16.part1()} (${elapsedTime(time)}ms)") // 52611030 
    time = System.nanoTime()
    println("Part 2: ${Y2019D16.part2()} (${elapsedTime(time)}ms)") // 52541026
}