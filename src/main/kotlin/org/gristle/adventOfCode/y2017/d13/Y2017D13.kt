package org.gristle.adventOfCode.y2017.d13

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D13(input: String) {

    private data class Layer(val depth: Int, val range: Int) {
        fun severity(): Int {
            return if ((depth) % ((range - 1) * 2) == 0) {
                depth * range
            } else 0
        }
        fun isTriggered(offset: Int) = (depth + offset) % ((range - 1) * 2) == 0
    }

    private val layers = input
        .groupValues("""(\d+): (\d+)""")
        .map { Layer(it[0].toInt(), it[1].toInt()) }


    var index = 0

    fun part1() = layers.sumOf { it.severity() }

    fun part2() = generateSequence(0) { it + 1 }
        .first { index -> layers.all { !it.isTriggered(index) } }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D13(readRawInput("y2017/d13"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1528
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 3896406
}