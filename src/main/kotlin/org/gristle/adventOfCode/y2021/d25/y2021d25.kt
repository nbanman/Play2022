package org.gristle.adventOfCode.y2021.d25

import org.gristle.adventOfCode.utilities.*

object Y2021D25 {
    private val lines = readInput("y2021/d25")

    data class Cucumbers(val east: Set<Coord>, val south: Set<Coord>, val size: Coord) {

        fun step(): Cucumbers {
            val newEast = east.map { p ->
                p.east(1, size, true).let { n -> if (n in east || n in south) p else n }
            }.toSet()
            val newSouth = south.map { p ->
                p.south(1, size, true).let { n -> if (n in newEast || n in south) p else n }
            }.toSet()
            return Cucumbers(newEast, newSouth, size)
        }
    }

    private fun makeCucumbers(): Cucumbers {

        val seaFloor = lines.flatMapIndexed { y, line -> line.mapIndexed{ x, ch -> Coord(x, y) to ch }}
        val east = seaFloor.mapNotNull { (coord, char) -> if (char == '>') coord else null }.toSet()
        val south = seaFloor.mapNotNull { (coord, char) -> if (char == 'v') coord else null }.toSet()
        val size = Coord(lines[0].length, lines.size)

        return Cucumbers(east, south, size)
    }

    tailrec fun part1(steps: Int = 1, prev: Cucumbers = makeCucumbers()): Int {
        val next = prev.step()
        return if (prev == next) steps else part1(steps + 1, next)
    }
}

fun main() {
    val time = System.nanoTime()
    println("Part 1: ${Y2021D25.part1()} (${elapsedTime(time)}ms)") // 528
}