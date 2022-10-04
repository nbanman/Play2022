package org.gristle.adventOfCode.y2018.d10

import org.gristle.adventOfCode.utilities.*

class Y2018D10(input: String) {

    data class Point(val pos: Coord, val vel: Coord)

    private val pattern = """position=< ?(-?\d+), +(-?\d+)> velocity=< ?(-?\d+), +(-?\d+)>""".toRegex()

    private val points = input
        .groupValues(pattern) { it.toInt() }
        .map { Point(Coord(it[0], it[1]), Coord(it[2], it[3])) }

    private fun List<Point>.move() = map { point -> point.copy(pos = point.pos + point.vel) }

    private val answer = generateSequence (points) { it.move() }
        .withIndex()
        .first { (_, points) ->
            val (_, yRange) = points.map { it.pos }.minMaxRanges()
            yRange.last - yRange.first == 9
        }
    
    fun part1() = answer.value.map { it.pos }.toString('.').ocr()
    fun part2() = answer.index
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D10(readRawInput("y2018/d10"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // LRCXFXRP
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 10630
}