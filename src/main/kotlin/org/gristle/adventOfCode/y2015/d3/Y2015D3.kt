package org.gristle.adventOfCode.y2015.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord

class Y2015D3(private val input: String) : Day {

    fun Coord.move(c: Char) = when (c) {
        '^' -> north()
        '>' -> east()
        'v' -> south()
        else -> west()
    }

    override fun part1() = input
        .runningFold(Coord.ORIGIN) { acc, c -> acc.move(c) }
        .toSet()
        .size

    override fun part2() = input
        .foldIndexed(mutableListOf(Coord(0, 0)) to mutableListOf(Coord(0, 0))) { index, acc, c ->
            val roboSanta = index % 2 == 1
            val listToAdd = if (roboSanta) acc.second else acc.first
            listToAdd.add(listToAdd.last().move(c))
            if (roboSanta) acc.first to listToAdd else listToAdd to acc.second
        }.let { it.first + it.second }
        .toSet()
        .size
}

fun main() = Day.runDay(Y2015D3::class)

//    Class creation: 21ms
//    Part 1: 2081 (8ms)
//    Part 2: 2341 (6ms)
//    Total time: 36ms