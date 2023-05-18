package org.gristle.adventOfCode.y2015.d25

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList

class Y2015D25(input: String) : Day {

    private val row: Int
    private val col: Int

    init {
        input.getIntList().let { (a, b) ->
            row = a
            col = b
        }
    }

    private fun getPlace(): Int {
        val extra = if (col == 1) {
            0
        } else {
            (row until row + col).reduce(Int::plus) - row
        }
        return (1 until row).reduce(Int::plus) + 1 + extra
    }

    override fun part1() = (2..getPlace()).fold(20151125L) { acc, _ -> (acc * 252533) % 33554393 }
    override fun part2() = true
}

fun main() = Day.runDay(Y2015D25::class)

//    Class creation: 16ms
//    Part 1: 8997277 (167ms)
//    Total time: 183ms
