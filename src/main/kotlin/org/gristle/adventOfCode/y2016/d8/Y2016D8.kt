package org.gristle.adventOfCode.y2016.d8

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.*

class Y2016D8(input: String) : Day {

    val pattern = """(rect|rotate (?:row|column)) (?:[xy]=)?(\d+)(?: by |x)(\d+)""".toRegex()

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

    private fun lightScreen(): Grid<Boolean> {
        val screen = MutableGrid(50, 6) { false }
        instructions.forEach { it.execute(screen) }
        return screen
    }

    private val screen = lightScreen()

    override fun part1() = screen.count { it }

    override fun part2() = screen.ocr()
}

fun main() = Day.runDay(Y2016D8::class)

//    Class creation: 24ms
//    Part 1: 123 (0ms)
//    Part 2: AFBUPZBJPS (5ms)
//    Total time: 30ms