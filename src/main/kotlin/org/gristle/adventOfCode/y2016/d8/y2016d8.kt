package org.gristle.adventOfCode.y2016.d8

import org.gristle.adventOfCode.utilities.*

object Y2016D8 {
    private val input = readRawInput("y2016/d8")

    val pattern = """(rect|rotate (?:row|column)) (?:[xy]=)?(\d+)(?: by |x)(\d+)"""

    data class Instruction(val mode: String, val arg1: Int, val arg2: Int) {
        fun execute(screen: MutableGrid<Boolean>) {
            when (mode) {
                "rect" -> for (y in 0 until arg2) for (x in 0 until arg1) screen[y * screen.width + x] = true
                "rotate column" -> screen
                    .column(arg1)
                    .shift(-arg2)
                    .forEachIndexed { index, b -> screen[arg1 + index * screen.width] = b }
                "rotate row" -> screen
                    .row(arg1)
                    .shift(-arg2)
                    .forEachIndexed { index, b -> screen[arg1 * screen.width + index] = b }
                else -> throw IllegalArgumentException()
            }
        }
    }

    val instructions = input.groupValues(pattern).map { Instruction(it[0], it[1].toInt(), it[2].toInt()) }

    fun lightScreen(width: Int, height: Int): Grid<Boolean> {
        val screen = List(width * height) { false }.toMutableGrid(width)
        instructions.forEach { it.execute(screen) }
        return screen
    }

    val screen = lightScreen(50, 6)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D8.screen.count { it } } (${elapsedTime(time)}ms)") // 123
    time = System.nanoTime()
    println("Part 2:")
    println("${Y2016D8.screen.representation { if (it) '#' else ' ' }} (${elapsedTime(time)}ms)") // AFBUPZBJPS
}