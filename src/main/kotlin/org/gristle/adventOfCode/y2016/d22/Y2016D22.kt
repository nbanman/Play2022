package org.gristle.adventOfCode.y2016.d22

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.groupValues

class Y2016D22(input: String) : Day {

    private val pattern = """(\d+)-y(\d+) +(\d+)T +(\d+)""".toRegex()

    data class Node(val coord: Coord, val size: Int, val used: Int, val garbage: Boolean) {
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
            Node(coord, it[2], it[3], coord == garbageCoord)
        }

    override fun part1() = nodes
        .filter { it.used != 0 }
        .sumOf { nodeA -> nodes.count { it != nodeA && it.available >= nodeA.used } }

    override fun part2() =
        "213, done by hand, as intended by puzzle author (see https://www.reddit.com/r/adventofcode/comments/5jor9q/comment/dbhwg4l/?utm_source=share&utm_medium=web2x&context=3)"
}

fun main() = Day.runDay(22, 2016, Y2016D22::class)

//Class creation: 32ms
//Part 1: 924 (20ms)
//Part 2: 213, done by hand, as intended by puzzle author (see https://www.reddit.com/r/adventofcode/comments/5jor9q/comment/dbhwg4l/?utm_source=share&utm_medium=web2x&context=3) (0ms)
//Total time: 53ms