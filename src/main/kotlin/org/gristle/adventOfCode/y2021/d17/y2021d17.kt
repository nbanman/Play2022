package org.gristle.adventOfCode.y2021.d17

import org.gristle.adventOfCode.utilities.*
import kotlin.math.abs

object Y2021D17 {
    private val input = readRawInput("y2021/d17")

    data class Vector(val pos: Coord, val velocity: Coord) {
        fun step(): Vector {
            val newXV = when {
                velocity.x > 0 -> velocity.x - 1
                velocity.x < 0 -> velocity.x + 1
                else -> velocity.x
            }
            return Vector(pos + velocity, Coord(newXV, velocity.y - 1))
        }
    }

    data class Box(val tl: Coord, val br: Coord) {

        fun ySteps(v: Vector): List<Int> {
            val timeOffset = if (v.velocity.y >= 0) v.velocity.y * 2 + 1 else 0
            val velocity = if (v.velocity.y >= 0) -(v.velocity.y + 1) else v.velocity.y
            val cromulent = mutableListOf<Int>()
            var shot = Vector(Coord(0, 0), Coord(0, velocity))
            var time = 0
            while (shot.pos.y >= br.y) {
                time++
                shot = shot.step()
                if (shot.pos.y in yRange) cromulent.add(time + timeOffset)
            }
            return cromulent
        }

        fun xValues(steps: Int): List<Int> {
            return (stallSpeed..br.x).filter { xVel ->
                (1..steps)
                    .fold(Vector(Coord(0, 0), Coord(xVel, 0))) { acc, _ -> acc.step() }
                    .pos.x in xRange
            }
        }

        val minY = br.y
        val maxY = (1 until abs(br.y)).sum()
        val yRange = br.y..tl.y
        val xRange = tl.x..br.x
        val stallSpeed = let {
            (5..br.x).first { (1..it).sum() in xRange }
        }

    }

    val intermediate = """(-?\d+)\.\.(-?\d+), y=(-?\d+)\.\.(-?\d+)"""
        .toRegex()
        .find(input)!!
        .groupValues
        .drop(1)
        .map { it.toInt() }

    val box = intermediate.let {
        val (x1, x2) = if (it[0] > it[1]) it[1] to it[0] else it[0] to it[1]
        val (y1, y2) = if (it[2] > it[3]) it[3] to it[2] else it[2] to it[3]
        Box(Coord(x1, y2), Coord(x2, y1))
    }

    fun part1() = box.maxY

    fun part2(): Int {
        val ys = (box.minY..box.maxY)
            .map { it to box.ySteps(Vector(Coord(0, 0), Coord(0, it))) }
            .filter { it.second.isNotEmpty() }
            .flatMap { pair -> pair.second.map { pair.first to it } }
            .map { it.first to box.xValues(it.second) }
            .flatMap { pair -> pair.second.map { Coord(pair.first, it) } }
            .distinct()
            .size
        return ys
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D17.part1()} (${elapsedTime(time)}ms)") // 17766 
    time = System.nanoTime()
    println("Part 2: ${Y2021D17.part2()} (${elapsedTime(time)}ms)") // 1733
}