package org.gristle.adventOfCode.y2015.d18

import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

class Y2015D18(input: String) {

    private val lights = input.toGrid { it == '#' }

    private fun Grid<Boolean>.iterate(cornersStuck: Boolean = false): Grid<Boolean> {
        val corners = if (cornersStuck) {
            listOf(0, width - 1, size - width, lastIndex)
        } else {
            emptyList()
        }

        return Grid(size, width) { i ->
            if (i in corners) {
                true
            } else {
                val neighborsOn = getNeighbors(i, true).count { it }
                neighborsOn == 3 || (neighborsOn == 2 && this[i])
            }
        }
    }

    private fun solve(cornersStuck: Boolean) = generateSequence(lights) { it.iterate(cornersStuck) }
        .take(101)
        .last()
        .count { it }

    fun part1() = solve(false)

    fun part2() = solve(true)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D18(readRawInput("y2015/d18"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1061
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1006
}