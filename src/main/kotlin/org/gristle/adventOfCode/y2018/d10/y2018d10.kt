package org.gristle.adventOfCode.y2018.d10

import org.gristle.adventOfCode.utilities.*

object Y2018D10 {
    private val input = readRawInput("y2018/d10")

    data class Point(val pos: Coord, val vel: Coord)

    private val pattern = """position=< ?(-?\d+), +(-?\d+)> velocity=< ?(-?\d+), +(-?\d+)>""".toRegex()

    private val points = input
        .groupValues(pattern) { it.toInt() }
        .map { Point(Coord(it[0], it[1]), Coord(it[2], it[3])) }

    private fun List<Point>.move() = map { point -> point.copy(pos = point.pos + point.vel) }

    private val answer = generateSequence (points) { it.move() }
        .withIndex()
        .first { indexedPoints ->
            val (_, yRange) = indexedPoints.value.map { it.pos }.minMaxRanges()
            yRange.last - yRange.first == 9
        }
    
    fun part1() = answer.value.map { it.pos }.toString('.').ocr()
    fun part2() = answer.index
}

fun main() {
    val time = System.nanoTime()
    println("Part 1: ${Y2018D10.part1()}") // LRCXFXRP
    println("Part 2: ${Y2018D10.part2()} (${elapsedTime(time)}ms)") // 10630
}