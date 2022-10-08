package org.gristle.adventOfCode.y2020.d24

import org.gristle.adventOfCode.utilities.Hexagon
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D24(input: String) {

    val pattern = "(nw|ne|sw|se|w|e)".toRegex()
    private val rules = input
        .lines()
        .map { line ->
            pattern.findAll(line)
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
    private val home = Hexagon()
    private val flipped: Map<Hexagon, Boolean> = buildMap {
        rules.forEach { rule ->
            val tile = rule.fold(home, Hexagon::hexAt)
            this[tile] = !computeIfAbsent(tile) { false }
        }
    }

    fun part1(): Int {
        return flipped.count { it.value }
    }

    fun part2(): Int {
        fun hexRing(n: Int): List<Hexagon> = buildList {
            for (r in 0..n) add(Hexagon(-n, r))
            for (q in -(n - 1)..0) {
                add(Hexagon(q, n))
                add(Hexagon(q, -(n + q)))
            }
            addAll(dropLast(2).map { hex -> Hexagon(-hex.q, -hex.r) })
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
                    blackNeighbors in 1..2
                } else {
                    blackNeighbors == 2
                }
            }
            flipMap = newMap
        }
        return flipMap.count { it.value }
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D24(readRawInput("y2020/d24"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 244
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 3665
}