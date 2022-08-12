package org.gristle.adventOfCode.y2018.d10

import org.gristle.adventOfCode.utilities.*

object Y2018D10 {
    private val input = readRawInput("y2018/d10")

    data class Point(val pos: Coord, val vel: Coord)

    private val pattern = """position=< ?(-?\d+), +(-?\d+)\> velocity=\< ?(-?\d+), +(-?\d+)\>""".toRegex()

    private val points = input
        .groupValues(pattern) { it.toInt() }
        .map { Point(Coord(it[0], it[1]), Coord(it[2], it[3])) }

    fun List<Point>.move() = map { point -> point.copy(pos = point.pos + point.vel) }

    fun solve() = generateSequence (points) { it.move() }
        .withIndex()
        .first { indexedPoints ->
            val (xRange, yRange) = indexedPoints.value.map { it.pos }.minMaxRanges()
            yRange.last - yRange.first == 9
        }
}

fun main() {
    val time = System.nanoTime()
    val answers = Y2018D10.solve()
    println("Part 1:")
    answers.value.map { it.pos }.printToConsole(' ') // LRCXFXRP
    println("Part 2: ${answers.index} (${elapsedTime(time)}ms)") // 10630

}