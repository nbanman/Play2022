package org.gristle.adventOfCode.y2018.d3

import org.gristle.adventOfCode.utilities.*

object Y2018D3 {
    private val input = readRawInput("y2018/d3")

    val pattern = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)"""

    data class Claim(val id: Int, val tl: Coord, val size: Coord) {
        val br = Coord(tl.x + size.x - 1, tl.y + size.y - 1)
        override fun toString(): String {
            return "Claim(id=$id, tl=$tl, size=$size, br=$br)"
        }
    }

    val claims = input
        .groupValues(pattern) { it.toInt() }
        .map { Claim(it[0], Coord(it[1], it[2]), Coord(it[3], it[4])) }

    val width = claims.maxOf { it.tl.x + it.size.x }
    val height = claims.maxOf { it.tl.y + it.size.y }

    private val skein = List<MutableSet<Int>>(width * height) { mutableSetOf() }.toGrid(width)

    fun part1(): Int {
        claims.forEach { claim ->
            Coord.forRectangle(claim.tl, claim.br) { x, y -> skein[Coord(x, y)].add(claim.id) }
        }
        return skein.count { it.size > 1 }
    }

    fun part2(): Int {
        val noOverlaps = skein.filter { it.size == 1 }.groupingBy { it.first() }.eachCount()
        return claims.first { claim ->
            claim.size.area() == noOverlaps.entries.find { it.key == claim.id }?.value
        }.id
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2018D3.part1()} (${elapsedTime(time)}ms)") // 110891
    time = System.nanoTime()
    println("Part 2: ${Y2018D3.part2()} (${elapsedTime(time)}ms)") // 297
}