package org.gristle.adventOfCode.y2017.d3

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

object Y2017D3 {
    private val input = readRawInput("y2017/d3")

    data class Turtle(var dir: Nsew = Nsew.NORTH, var pos: Coord = Coord.ORIGIN, var vel: Int)

    fun part1(): Int {
        val squareRoot = ceil(sqrt(input.toFloat())).toInt().let { if (it % 2 == 0) it + 1 else it }
        val furthest = (squareRoot / 2) * 2
        val br = squareRoot * squareRoot
        val diff = (br - input.toInt()) % furthest
        return furthest - min(diff, furthest / 2) + max(0, diff - furthest / 2)
    }

    fun part2(): Int {
        val t = Turtle(Nsew.SOUTH, Coord.ORIGIN, 0)
        val space = mutableMapOf(t.pos to 1)
        while (true) {
            t.dir = t.dir.left()
            if (t.dir == Nsew.EAST || t.dir == Nsew.WEST) t.vel++
            for (i in 1..t.vel) {
                t.pos = t.pos.move(t.dir)
                val squareVal = t.pos.getNeighbors(true).sumOf { space[it] ?: 0 }
                if (squareVal > input.toInt()) return squareVal
                space[t.pos] = squareVal
            }
        }
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D3.part1()} (${elapsedTime(time)}ms)") // 552
    time = System.nanoTime()
    println("Part 2: ${Y2017D3.part2()} (${elapsedTime(time)}ms)") // 330785
}