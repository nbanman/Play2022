package org.gristle.adventOfCode.y2018.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.getInts

class Y2018D3(input: String) : Day {

    data class Claim(val id: Int, val tl: Coord, val size: Coord) {
        val br = Coord(tl.x + size.x - 1, tl.y + size.y - 1)
        override fun toString(): String {
            return "Claim(id=$id, tl=$tl, size=$size, br=$br)"
        }
    }

    private val claims = input
        .getInts()
        .chunked(5)
        .map { Claim(it[0], Coord(it[1], it[2]), Coord(it[3], it[4])) }
        .toList()

    private val width = claims.maxOf { it.tl.x + it.size.x }
    private val height = claims.maxOf { it.tl.y + it.size.y }

    private val skein = Grid<MutableSet<Int>>(width, height) { mutableSetOf() }

    init {
        claims.forEach { claim ->
            Coord.forRectangle(claim.tl, claim.br) { skein[it].add(claim.id) }
        }
    }


    override fun part1() = skein.count { it.size > 1 }

    override fun part2(): Int {
        val noOverlaps = skein.filter { it.size == 1 }.groupingBy { it.first() }.eachCount()
        return claims.first { claim ->
            claim.size.area() == noOverlaps.entries.find { it.key == claim.id }?.value
        }.id
    }
}

fun main() = Day.runDay(3, 2018, Y2018D3::class)

//    Class creation: 148ms
//    Part 1: 110891 (15ms)
//    Part 2: 297 (164ms)
//    Total time: 327ms
