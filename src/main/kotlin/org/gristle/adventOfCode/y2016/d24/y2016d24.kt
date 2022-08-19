package org.gristle.adventOfCode.y2016.d24

import org.gristle.adventOfCode.utilities.*

object Y2016D24 {

    private val layout = readRawInput("y2016/d24")
        .toGrid()

    private val numbers = layout.withIndex().filter { it.value.isDigit() }

    private val distances = numbers
        .map { (index, number) ->
            val distances = Graph.bfs(index to layout[index]) { (index, _) ->
                layout
                    .getNeighborIndices(index)
                    .filter { layout[it] != '#' }
                    .map { it to layout[it] }
            }.drop(1)
                .filter { dest -> dest.id.second.isDigit() } // && dest.path().count { it.id.second.isDigit() } == 2 }
            number to distances.map { it.id.second to it.weight }
        }

    private val edges = buildMap {
        distances.forEach { (from, toList) ->
            toList.forEach { (dest, weight) -> this[from to dest] = weight }
        }
    }

    fun part1(): Int {
        val combos = numbers.map { it.value }.getPermutations(listOf('0'))
        return combos.minOf { combo -> combo.windowed(2).sumOf { (f, s) -> edges[f to s] ?: 0.0 } }.toInt()
    }

    fun part2(): Int {
        val combos = numbers.map { it.value }.getPermutations(listOf('0')).map { it + '0' }
        return combos.minOf { combo -> combo.windowed(2).sumOf { (f, s) -> edges[f to s] ?: 0.0 } }.toInt()
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D24.part1()} (${elapsedTime(time)}ms)") // 470
    time = System.nanoTime()
    println("Part 2: ${Y2016D24.part2()} (${elapsedTime(time)}ms)") // 720
}

