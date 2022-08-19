package org.gristle.adventOfCode.y2016.d22

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

object Y2016D22 {
    private val input = readRawInput("y2016/d22")

    private val pattern = """(\d+)-y(\d+) +(\d+)T +(\d+)""".toRegex()

    data class ProblemNode(val coord: Coord, val size: Int, val used: Int, val garbage: Boolean) {
        val available = size - used

        override fun toString(): String {
            return "(x:${coord.x}, y:${coord.y}, size:$size, used:$used, available:$available, garbage:$garbage)"
        }
    }

    val gv = input.groupValues(pattern) { it.toInt() }

    private val garbageCoord = Coord(gv.last()[0], 0)

    private val nodes = gv
        .map {
            val coord = Coord(it[0], it[1])
            ProblemNode(coord, it[2], it[3], coord == garbageCoord)
        }

    fun part1() = nodes
        .filter { it.used != 0 }
        .sumOf { nodeA -> nodes.count { it != nodeA && it.available >= nodeA.used } }
}

fun main() {
    val time = System.nanoTime()
    println("Part 1: ${Y2016D22.part1()} (${elapsedTime(time)}ms)") // 924
    println("Part 2: 213, done by hand, as intended by puzzle author (see https://www.reddit.com/r/adventofcode/comments/5jor9q/comment/dbhwg4l/?utm_source=share&utm_medium=web2x&context=3)") // 213
    println("(also, I gave up!)")
}