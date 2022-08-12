package org.gristle.adventOfCode.y2015.d25

import org.gristle.adventOfCode.utilities.*

object Template {
    private val input = readRawInput("y2015/d25")

    private val pattern = """(\d+)""".toRegex()

    val matches = pattern.findAll(input)

    val row = matches.first().value.toInt()

    val col = matches.last().value.toInt()

    fun getCol1(row: Int): Int {
        return if (row == 1) {
            1
        } else {
            (1 until row).reduce { acc, i -> acc + i } + 1
        }
    }

    fun getPlace(row: Int, col: Int): Int {
        val col1 = getCol1(row)
        val extra = if (col == 1) {
            0
        } else {
            (row until row + col).reduce { acc, i -> acc + i } - (row)
        }
        return col1 + extra
    }

    fun part1() = (2..getPlace(row, col)).fold(20151125L) { acc, _ -> (acc * 252533) % 33554393 }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Template.part1()} (${elapsedTime(time)}ms)") // 8997277
}