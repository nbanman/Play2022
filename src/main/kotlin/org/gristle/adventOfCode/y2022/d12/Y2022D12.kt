package org.gristle.adventOfCode.y2022.d12

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.toGrid

class Y2022D12(input: String) {

    private val area = input.toGrid()

    private val vertices = Graph
        .bfs(area.indexOf('E')) { pos ->
            area
                .getNeighborsIndexedValue(pos)
                .filter { (_, c) -> c.height() >= area[pos].height() - 1 }
                .map { (index, _) -> index }
        }.filter { area[it.id] in "Sa" }

    private fun Char.height() = when (this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    }.code

    fun solve(starts: String) = vertices
        .filter { area[it.id] in starts }
        .minOf { it.weight.toInt() }

    fun part1() = solve("S")
    fun part2() = solve("Sa")
}

fun main() {
    val input = getInput(12, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D12(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 361
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 354
    println("Total time: ${timer.elapsed()}ms")
}