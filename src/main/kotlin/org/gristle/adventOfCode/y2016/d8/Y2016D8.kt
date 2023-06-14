package org.gristle.adventOfCode.y2016.d8

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.*

class Y2016D8(input: String) : Day {

    private fun MutableGrid<Boolean>.executeInstruction(instruction: String) {
        val (n1: Int, n2: Int) = instruction.getIntList()
        when (instruction.replaceFirst("rotate ", "").takeWhile { it != ' ' }) {
            "rect" -> Coord
                .rectangleFrom(Coord.ORIGIN, Coord(n1 - 1, n2 - 1))
                .forEach { this[it] = true }

            "column" -> this
                .column(n1)
                .shift(-n2)
                .forEachIndexed { y, b -> this[n1, y] = b }

            "row" -> this
                .row(n1)
                .shift(-n2)
                .forEachIndexed { x, b -> this[x, n1] = b }
        }
    }

    private val screen: MutableGrid<Boolean> by lazy {
        MutableGrid(50, 6) { false }
            .apply { input.lineSequence().forEach { instruction -> executeInstruction(instruction) } }
    }

    override fun part1() = screen.count { it }

    override fun part2() = screen.ocr()
}

fun main() = Day.runDay(Y2016D8::class)

//    Class creation: 24ms
//    Part 1: 123 (0ms)
//    Part 2: AFBUPZBJPS (5ms)
//    Total time: 30ms