package org.gristle.adventOfCode.y2018.d3

import org.gristle.adventOfCode.utilities.*

class Y2018D3(input: String) {

    val pattern = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()

    data class Claim(val id: Int, val tl: Coord, val size: Coord) {
        val br = Coord(tl.x + size.x - 1, tl.y + size.y - 1)
        override fun toString(): String {
            return "Claim(id=$id, tl=$tl, size=$size, br=$br)"
        }
    }

    private val claims = input
        .groupValues(pattern, String::toInt)
        .map { Claim(it[0], Coord(it[1], it[2]), Coord(it[3], it[4])) }

    private val width = claims.maxOf { it.tl.x + it.size.x }
    private val height = claims.maxOf { it.tl.y + it.size.y }

    private val skein = Grid<MutableSet<Int>>(width, height) { mutableSetOf() }

    init {
        claims.forEach { claim ->
            Coord.forRectangle(claim.tl, claim.br) { skein[it].add(claim.id) }
        }
    }
    

    fun part1() = skein.count { it.size > 1 }

    fun part2(): Int {
        val noOverlaps = skein.filter { it.size == 1 }.groupingBy { it.first() }.eachCount()
        return claims.first { claim ->
            claim.size.area() == noOverlaps.entries.find { it.key == claim.id }?.value
        }.id
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D3(readRawInput("y2018/d3"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 110891
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 297
}