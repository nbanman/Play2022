package org.gristle.adventOfCode.y2019.d16

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toDigit
import kotlin.math.abs

class Y2019D16(private val input: String) : Day {
    private val nos = input.map(Char::toDigit)
    private val phases = 100

    override fun part1(): Int {
        val startPattern = listOf(0, 1, 0, -1)
        return (1..phases).fold(nos) { acc, _ ->
            List(acc.size) { index ->
                acc.foldIndexed(0) { index2, acc2, ii ->
                    val ai = ((index2 + 1) / (index + 1)) % 4
                    acc2 + ii * startPattern[ai]
                }.let { abs(it % 10) }
            }
        }.take(8)
            .joinToString("")
            .toInt()
    }

    override fun part2(): Int {
        val offset = input.take(7).toInt()
        val arraySize = nos.size * 10_000 - offset
        val offsetMod = offset % nos.size
        val nosi = IntArray(arraySize) { i -> nos[(offsetMod + i) % nos.size] }
        repeat(phases) {
            for (i in nosi.lastIndex - 1 downTo 0) {
                nosi[i] = (nosi[i] + nosi[i + 1]) % 10
            }
        }
        return nosi
            .take(8)
            .joinToString("")
            .toInt()
    }
}

fun main() = Day.runDay(Y2019D16::class)
var time = System.nanoTime()
val c = Y2019D16(readRawInput("y2019/d16"))

//    Class creation: 11ms
//    Part 1: 52611030 (206ms)
//    Part 2: 52541026 (174ms)
//    Total time: 391ms