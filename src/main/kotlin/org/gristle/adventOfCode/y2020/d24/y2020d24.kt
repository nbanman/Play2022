package org.gristle.adventOfCode.y2020.d24

import org.gristle.adventOfCode.utilities.*

object Y2020D24 {
    private val input = readInput("y2020/d24")

    val pattern = "(nw|ne|sw|se|w|e)".toRegex()
    val rules = input
        .map { line -> pattern.
        findAll(line)
            .toList()
            .map {
                when (it.value) {
                    "w" -> "n"
                    "nw" -> "ne"
                    "ne" -> "se"
                    "e" -> "s"
                    "se" -> "sw"
                    "sw" -> "nw"
                    else -> it.value
                }
            }
        }
    val home = Hexagon()
    val flipped = mutableMapOf<Hexagon, Boolean>()
        .also {
            rules.forEach { rule ->
                var start = home
                val tile = rule.fold(start) { acc, move -> acc.hexAt(move) }
                it[tile] = !it.computeIfAbsent(tile) { false }
            }
        }.toMap()

    fun part1(): Int {
        return flipped.count { it.value }
    }

    fun part2(): Int {
        fun hexRing(n: Int): List<Hexagon> {
            return mutableListOf<Hexagon>().apply {
                for (r in 0..n) add(Hexagon(-n, r))
                for (q in -(n - 1)..0) {
                    add(Hexagon(q, n))
                    add(Hexagon(q, -(n + q)))
                }
            }.let { it + it.dropLast(2).map { hex -> Hexagon(-hex.q, -hex.r) } }
        }

        var flipMap = flipped
        var radius = flipMap.filter { it.value }.maxOf { it.key.distance(home) }
        for (day in 1..100) {
            val newMap = flipMap.toMutableMap()
            radius++
            val hexen = (1..radius).fold(listOf(home)) { acc, ring ->
                acc + hexRing(ring)
            }
            hexen.forEach { hexagon ->
                val blackNeighbors = hexagon.neighbors().count { flipMap[it] ?: false }
                val isBlack = flipMap[hexagon] ?: false
                newMap[hexagon] = if (isBlack) { // if black
//                    println("$hexagon is black, has $blackNeighbors black neighbors, so ${ if(blackNeighbors in 1..2)"stays black" else "becomes white" }")
                    blackNeighbors in 1..2
                } else {
//                    println("$hexagon is white, has $blackNeighbors black neighbors, so ${ if(blackNeighbors == 2)"becomes black" else "stays white" }")
                    blackNeighbors == 2
                }
            }
//            println("day $day: ${flipMap.count { it.value } }")
            flipMap = newMap
        }
        return flipMap.count { it.value }
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D24.part1()} (${elapsedTime(time)}ms)") // 244
    time = System.nanoTime()
    println("Part 2: ${Y2020D24.part2()} (${elapsedTime(time)}ms)") // 3665
}