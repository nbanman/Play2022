package org.gristle.adventOfCode.y2015.d25

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D25(input: String) {

    private val pattern = """(\d+)""".toRegex()

    private val matches = pattern.findAll(input)

    private val row = matches.first().value.toInt()

    private val col = matches.last().value.toInt()

    private fun getPlace(): Int {
        val extra = if (col == 1) {
            0
        } else {
            (row until row + col).reduce(Int::plus) - row
        }
        return (1 until row).reduce(Int::plus) + 1 + extra
    }

    fun part1() = (2..getPlace()).fold(20151125L) { acc, _ -> (acc * 252533) % 33554393 }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D25(readRawInput("y2015/d25"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 8997277
}