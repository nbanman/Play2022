package org.gristle.adventOfCode.y2016.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import kotlin.math.abs
import kotlin.math.sign

class Y2016D2(input: String) : Day {
    private val codes = input.lines()

    fun solve(
        start: Coord,
        padTraverse: (Coord, Char) -> Coord,
        conversion: Coord.() -> String
    ): String {
        val coords: List<Coord> = codes
            .runningFold(start) { lastPos, s ->
                s.fold(lastPos, padTraverse)
            }
        return coords
            .drop(1)
            .joinToString(separator = "", transform = conversion)
    }

    override fun part1(): String {
        val start = Coord(1, 1)
        val size = Coord(3, 3)
        val padTraverse = { coord: Coord, c: Char ->
            when (c) {
                'R' -> coord.east(1, size)
                'L' -> coord.west(1, size)
                'U' -> coord.north(1, size)
                else -> coord.south(1, size)
            }
        }

        fun Coord.toNumpad1(): String = (y * 3 + x + 1).toString()

        return solve(start, padTraverse, Coord::toNumpad1)
    }

    override fun part2(): String {
        val start = Coord(0, 2)
        val size = Coord(5, 5)
        val padTraverse = { coord: Coord, c: Char ->
            val xRestrict = abs(coord.y - 2)
            val yRestrict = abs(coord.x - 2)
            when (c) {
                'R' -> if (coord.x + 1 > 4 - xRestrict) {
                    coord
                } else {
                    coord.east(1, size)
                }

                'L' -> if (coord.x - 1 < xRestrict) {
                    coord
                } else {
                    coord.west(1, size)
                }

                'U' -> if (coord.y - 1 < yRestrict) {
                    coord
                } else {
                    coord.north(1, size)
                }

                else -> if (coord.y + 1 > 4 - yRestrict) {
                    coord
                } else {
                    coord.south(1, size)
                }
            }
        }

        fun Coord.toNumpad2() = (5 + x + (y - 2) * 2 + 2 * (y - 2).sign).toString(16).uppercase()

        return solve(start, padTraverse, Coord::toNumpad2)
    }
}

fun main() = Day.runDay(Y2016D2::class)

//    Class creation: 16ms
//    Part 1: 92435 (6ms)
//    Part 2: C1A88 (2ms)
//    Total time: 25ms