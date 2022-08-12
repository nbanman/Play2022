package org.gristle.adventOfCode.y2020.d3

import org.gristle.adventOfCode.utilities.*

object Y2020D3 {
    private val input = readRawInput("y2020/d3")

    val grid = input.toGrid()

    fun part1(right: Int = 3, down: Int = 1) =
        (down until grid.height step down).foldIndexed(0) { i, acc, y ->
            acc + if(grid[Coord(((i + 1) * right) % grid.width, y)] == '#') 1 else 0
        }

    fun part2() = listOf(
        part1(1),
        part1(3),
        part1(5),
        part1(7),
        part1(1, 2)
    ).fold(1L) { acc, i -> acc * i }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D3.part1()} (${elapsedTime(time)}ms)") // 294
    time = System.nanoTime()
    println("Part 2: ${Y2020D3.part2()} (${elapsedTime(time)}ms)") // 5774564250
}