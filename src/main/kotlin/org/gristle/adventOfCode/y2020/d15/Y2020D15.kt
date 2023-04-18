package org.gristle.adventOfCode.y2020.d15

import org.gristle.adventOfCode.Day

class Y2020D15(input: String) : Day {

    val start = input.split(',').map(String::toInt)

    data class Spokenses(val first: Int? = null, val last: Int? = null) {

        companion object {
            val DEFAULT = Spokenses()
        }

        fun update(n: Int): Spokenses {
            return when {
                first == null -> Spokenses(n, null)
                last == null -> Spokenses(first, n)
                else -> Spokenses(last, n)
            }
        }
    }

    private fun lastNumberSpoken(iterations: Int): Int {

        val log = start
            .mapIndexed { index, i -> i to Spokenses(index + 1) }
            .toTypedArray()
            .let { mutableMapOf(*it) }

        var lastNumberSpoken = start.last()

        for (turn in (start.size + 1)..iterations) {
            val spoke = log.getValue(lastNumberSpoken)
            lastNumberSpoken = if (spoke.last == null || spoke.first == null) {
                0
            } else {
                spoke.last - spoke.first
            }
            log[lastNumberSpoken] = log.getOrDefault(lastNumberSpoken, Spokenses.DEFAULT).update(turn)
        }
        return lastNumberSpoken
    }

    override fun part1() = lastNumberSpoken(2020)

    override fun part2() = lastNumberSpoken(30_000_000)
}

fun main() = Day.runDay(Y2020D15::class)

//    Class creation: 17ms
//    Part 1: 929 (2ms)
//    Part 2: 16671510 (5581ms)
//    Total time: 5601ms