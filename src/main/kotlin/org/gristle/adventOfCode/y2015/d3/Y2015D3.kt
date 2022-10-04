package org.gristle.adventOfCode.y2015.d3

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D3(private val input: String) {

    fun Coord.move(c: Char) = when (c) {
        '^' -> north()
        '>' -> east()
        'v' -> south()
        else -> west()
    }

    fun part1() = input
        .fold(mutableListOf(Coord(0, 0))) { acc, c ->
            acc.apply { add(acc.last().move(c)) }
        }.toSet()
        .size

    fun part2() = input
        .foldIndexed(mutableListOf(Coord(0, 0)) to mutableListOf(Coord(0, 0))) { index, acc, c ->
            val roboSanta = index % 2 == 1
            val listToAdd = if (roboSanta) acc.second else acc.first
            listToAdd.add(listToAdd.last().move(c))
            if (roboSanta) acc.first to listToAdd else listToAdd to acc.second
        }.let { it.first + it.second }
        .toSet()
        .size
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D3(readRawInput("y2015/d3"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 2081
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2341
}