package org.gristle.adventOfCode.y2015.d3

import org.gristle.adventOfCode.utilities.*

object Y2015D3 {
    private val input = readRawInput("y2015/d3")

    fun part1() = input.fold(mutableListOf(Coord(0, 0))) { acc, c ->
        val last = acc.last()
        acc.add(
            when (c) {
                '^' -> last.north()
                '>' -> last.east()
                'v' -> last.south()
                else -> last.west()
            }
        )
        acc
    }.toSet().size

    fun part2() = input
        .foldIndexed(mutableListOf(Coord(0, 0)) to mutableListOf(Coord(0, 0))) { index, acc, c ->
            val roboSanta = index % 2 == 1
            val listToAdd = if (roboSanta) acc.second else acc.first
            val last = listToAdd.last()
            listToAdd.add(
                when (c) {
                    '^' -> last.north()
                    '>' -> last.east()
                    'v' -> last.south()
                    else -> last.west()
                }
            )
            if (roboSanta) acc.first to listToAdd else listToAdd to acc.second
        }.let { it.first + it.second }
        .toSet()
        .size
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D3.part1()} (${elapsedTime(time)}ms)") // 2081
    time = System.nanoTime()
    println("Part 2: ${Y2015D3.part2()} (${elapsedTime(time)}ms)") // 2341
}