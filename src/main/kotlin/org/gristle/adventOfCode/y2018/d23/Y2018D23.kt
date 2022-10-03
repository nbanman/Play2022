package org.gristle.adventOfCode.y2018.d23

import org.gristle.adventOfCode.utilities.*
import java.util.*
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.pow

class Y2018D23(input: String) {

    data class Nanobot(val pos: Xyz, val radius: Int) {
        fun inRangeOf(other: Nanobot) = radius >= pos.manhattanDistance(other.pos)
        private fun inRangeOf(other: Xyz) = radius >= pos.manhattanDistance(other)
        fun inRangeOf(cube: Cube): Boolean {
            return Xyz(
                when {
                    pos.x in cube.pos.x until cube.pos.x + cube.length -> {
                        pos.x
                    }
                    pos.x < cube.pos.x -> {
                        cube.pos.x
                    }
                    else -> {
                        cube.pos.x + cube.length - 1
                    }
                },
                when {
                    pos.y in cube.pos.y until cube.pos.y + cube.length -> {
                        pos.y
                    }
                    pos.y < cube.pos.y -> {
                        cube.pos.y
                    }
                    else -> {
                        cube.pos.y + cube.length - 1
                    }
                },
                when {
                    pos.z in cube.pos.z until cube.pos.z + cube.length -> {
                        pos.z
                    }
                    pos.z < cube.pos.z -> {
                        cube.pos.z
                    }
                    else -> {
                        cube.pos.z + cube.length - 1
                    }
                }
            ).let { inRangeOf(it) }
        }
    }

    class Cube(val pos: Xyz, val length: Int, parentBots: List<Nanobot>) {
        val nanobots = parentBots.filter { it.inRangeOf(this) }

        fun cubify(): List<Cube> {
            val newLength = length / 2
            return listOf(
                Cube(Xyz(pos.x, pos.y, pos.z), newLength, nanobots),
                Cube(Xyz(pos.x + newLength, pos.y, pos.z), newLength, nanobots),
                Cube(Xyz(pos.x, pos.y + newLength, pos.z), newLength, nanobots),
                Cube(Xyz(pos.x + newLength, pos.y + newLength, pos.z), newLength, nanobots),
                Cube(Xyz(pos.x, pos.y, pos.z + newLength), newLength, nanobots),
                Cube(Xyz(pos.x + newLength, pos.y, pos.z + newLength), newLength, nanobots),
                Cube(Xyz(pos.x, pos.y + newLength, pos.z + newLength), newLength, nanobots),
                Cube(Xyz(pos.x + newLength, pos.y + newLength, pos.z + newLength), newLength, nanobots)
            )
        }

        override fun toString(): String {
            return "Cube: $pos, Length: $length, Bots: ${nanobots.size}"
        }
    }

    private val pattern = """pos=<(-?\d+),(-?\d+),(-?\d+)>, r=(\d+)""".toRegex()

    private val nanobots = input
        .groupValues(pattern) { it.toInt() }
        .map { Nanobot(Xyz(it[0], it[1], it[2]), it[3]) }

    fun part1(): Int {
        val strongest = nanobots.maxByOrNull { it.radius } ?: return -1
        return nanobots.count { strongest.inRangeOf(it) }
    }

    fun part2(): Int {
        val xMin = nanobots.minOf { it.pos.x }
        val xMax = nanobots.maxOf { it.pos.x }
        val yMin = nanobots.minOf { it.pos.y }
        val yMax = nanobots.maxOf { it.pos.y }
        val zMin = nanobots.minOf { it.pos.z }
        val zMax = nanobots.maxOf { it.pos.z }

        val (min, max) = listOf(xMin to xMax, yMin to yMax, zMin to zMax)
            .maxByOrNull { it.second - it.first }!!

        val length = 2.0f.pow(ceil(ln((max - min).toFloat()) / ln(2.0f))).toInt()
        val initialCube = Cube(Xyz(min, min, min), length, nanobots)
        val cubes = PriorityQueue(compareBy<Cube> { it.length }.thenByDescending { it.nanobots.size })
        cubes.add(initialCube)
        var current = Cube(Xyz(0, 0, 0), 1, nanobots)
        while (true) {
            val next = cubes
                .pollUntil { it.nanobots.size > current.nanobots.size }
                ?.cubify() ?: break
            for (c in next) {
                current = if (c.length == 1 && c.nanobots.size > current.nanobots.size) c else current
                cubes.add(c)
            }
        }
        return current.pos.manhattanDistance(Xyz(0,0,0))
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D23(readRawInput("y2018/d23"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 481
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 47141479
}
