package org.gristle.adventOfCode.y2015.d25

import org.gristle.adventOfCode.Day

class Y2015D25(input: String) : Day {

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

    override fun part1() = (2..getPlace()).fold(20151125L) { acc, _ -> (acc * 252533) % 33554393 }
    override fun part2() = true
}

fun main() = Day.runDay(Y2015D25::class)

//    Class creation: 16ms
//    Part 1: 8997277 (167ms)
//    Total time: 183ms
