package org.gristle.adventOfCode.y2018.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.getInts

class Y2018D3(input: String) : Day {
    private val claims = input
        .getInts()
        .chunked(5) { (_, x, y, w, h) -> Coord(x, y) to Coord(x + w - 1, y + h - 1) }
        .toList()

    private val width = claims.maxOf { (_, br) -> br.x } + 1
    private val height = claims.maxOf { (_, br) -> br.y } + 1

    private val skein = IntArray(width * height).apply {
        claims.forEach { (tl, br) ->
            Coord.forRectangle(tl, br) {
                this[it.asIndex(width)]++
            }
        }
    }

    override fun part1(): Int = skein.count { it > 1 }

    override fun part2(): Int = 1 + claims
        .indexOfFirst { (tl, br) ->
            Coord.rectangleFrom(tl, br).all { pos ->
                skein[pos.asIndex(width)] == 1
            }
        }

}

fun main() = Day.runDay(Y2018D3::class)

//    Class creation: 32ms
//    Part 1: 110891 (4ms)
//    Part 2: 297 (13ms)
//    Total time: 49ms
