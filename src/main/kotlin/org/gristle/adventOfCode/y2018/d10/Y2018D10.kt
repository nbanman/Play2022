package org.gristle.adventOfCode.y2018.d10

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.*

class Y2018D10(input: String) : Day {

    data class MovingPoint(val pos: Coord, val vel: Coord)

    private val pattern = """position=< ?(-?\d+), +(-?\d+)> velocity=< ?(-?\d+), +(-?\d+)>""".toRegex()

    private val points = input
        .groupValues(pattern, String::toInt)
        .map { MovingPoint(Coord(it[0], it[1]), Coord(it[2], it[3])) }

    private fun List<MovingPoint>.move() = map { point -> point.copy(pos = point.pos + point.vel) }

    private val answer = generateSequence(points) { it.move() }
        .withIndex()
        .first { (_, points) ->
            val (_, yRange) = points.map(MovingPoint::pos).minMaxRanges()
            yRange.last - yRange.first == 9
        }

    override fun part1() = answer.value.map(MovingPoint::pos).toGraphicString('.').ocr()
    override fun part2() = answer.index
}

fun main() = Day.runDay(Y2018D10::class)

//    Class creation: 187ms
//    Part 1: LRCXFXRP (10ms)
//    Part 2: 10630 (0ms)
//    Total time: 198ms