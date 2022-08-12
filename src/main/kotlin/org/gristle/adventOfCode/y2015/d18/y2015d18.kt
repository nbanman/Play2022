package org.gristle.adventOfCode.y2015.d18

import org.gristle.adventOfCode.utilities.*

object Y2015D18 {
    private val input = readRawInput("y2015/d18")

    private val lights = input.toGrid { it == '#' }

    private fun Grid<Boolean>.iterate(cornersStuck: Boolean = false): Grid<Boolean> {
        val corners = if (cornersStuck) {
            listOf(0, width - 1, size - width, lastIndex)
        } else {
            emptyList()
        }

        return List(size) { i ->
            if (i in corners) {
                true
            } else {
                val neighborsOn = getNeighbors(i, true).count { it }
                neighborsOn == 3 || (neighborsOn == 2 && this[i])
            }
        }.toGrid(width)
    }

    private fun solve(steps: Int, cornersStuck: Boolean) = generateSequence(lights) { it.iterate(cornersStuck) }
        .take(steps + 1)
        .last()
        .count { it }

    fun part1() = solve(100, false)

    fun part2() = solve(100, true)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D18.part1()} (${elapsedTime(time)}ms)") // 1061
    time = System.nanoTime()
    println("Part 2: ${Y2015D18.part2()} (${elapsedTime(time)}ms)") // 1006
}