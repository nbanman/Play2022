package org.gristle.adventOfCode.y2016.d2

import org.gristle.adventOfCode.utilities.*
import kotlin.math.abs

object Template {
    private val codes = readInput("y2016/d2")

    fun Coord.toNumpad1() = (y * 3 + x + 1).toString()

    fun Coord.toNumpad2() = when(this) {
        Coord(2, 0) -> "1"
        Coord(1, 1) -> "2"
        Coord(2, 1) -> "3"
        Coord(3, 1) -> "4"
        Coord(0, 2) -> "5"
        Coord(1, 2) -> "6"
        Coord(2, 2) -> "7"
        Coord(3, 2) -> "8"
        Coord(4, 2) -> "9"
        Coord(1, 3) -> "A"
        Coord(2, 3) -> "B"
        Coord(3, 3) -> "C"
        Coord(2, 4) -> "D"
        else -> throw IllegalArgumentException()
    }

    fun part1(): String {
        val size = Coord(3, 3)
        val coords = codes.fold(listOf(Coord(1, 1))) { coordList, s ->
            coordList + s.fold(coordList.last()) { coord, c ->
                when(c) {
                    'R' -> coord.east(1, size)
                    'L' -> coord.west(1, size)
                    'U' -> coord.north(1, size)
                    else -> coord.south(1, size)
                }
            }
        }
        return coords.drop(1).joinToString("") { it.toNumpad1() }
    }

    fun part2(): String {
        val size = Coord(5, 5)
        val coords = codes.fold(listOf(Coord(2, 2))) { coordList, s ->
            coordList + s.fold(coordList.last()) { coord, c ->
                val xRestrict = abs(coord.y - 2)
                val yRestrict = abs(coord.x - 2)
                when(c) {
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
        }
        return coords.drop(1).joinToString("") { it.toNumpad2() }
    }}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Template.part1()} (${elapsedTime(time)}ms)") // 92435
    time = System.nanoTime()
    println("Part 2: ${Template.part2()} (${elapsedTime(time)}ms)") // C1A88
}