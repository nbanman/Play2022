package org.gristle.adventOfCode.y2015.d25

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

object Y2015D25 {
    private val input = readRawInput("y2015/d25")

    private val pattern = """(\d+)""".toRegex()

    private val matches = pattern.findAll(input)

    private val row = matches.first().value.toInt()

    private val col = matches.last().value.toInt()

    private fun getCol1(): Int {
        return if (row == 1) {
            1
        } else {
            (1 until row).reduce { acc, i -> acc + i } + 1
        }
    }

    private fun getPlace(): Int {
        val col1 = getCol1()
        val extra = if (col == 1) {
            0
        } else {
            (row until row + col).reduce { acc, i -> acc + i } - (row)
        }
        return col1 + extra
    }

    fun part1() = (2..getPlace()).fold(20151125L) { acc, _ -> (acc * 252533) % 33554393 }
}

fun main() {
    val time = System.nanoTime()
    println("Part 1: ${Y2015D25.part1()} (${elapsedTime(time)}ms)") // 8997277
}