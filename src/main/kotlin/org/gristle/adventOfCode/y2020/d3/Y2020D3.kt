package org.gristle.adventOfCode.y2020.d3

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

class Y2020D3(input: String) {

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
    ).fold(1L, Long::times)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D3(readRawInput("y2020/d3"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 294
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 5774564250
}