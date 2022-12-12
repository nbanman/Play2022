package org.gristle.adventOfCode.y2022.d12

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.toGrid

class Y2022D12(input: String) {

    private val area = input.toGrid()

    private fun Char.height() = when (this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    }.code

    private fun getEdges(pos: Int): List<Int> = area
        .getNeighborsIndexedValue(pos)
        .filter { (_, c) -> c.height() - 1 <= area[pos].height() }
        .map { (index, _) -> index }

    private val endCondition = { pos: Int -> area[pos] == 'E' }

    fun solve(starts: List<Char>): Int {
        val startingPoints = area.withIndex().filter { (_, c) -> c in starts }.map { (index, _) -> index }
        return startingPoints
            .map { startId ->
                Graph.dfs(
                    startId = startId,
                    endCondition = endCondition,
                    defaultEdges = ::getEdges
                ).steps()
            }.filter { it > 0 }
            .min()
    }

    fun part1() = solve(listOf('S'))
    fun part2() = solve(listOf('S', 'a'))
}

fun main() {
    val input = getInput(12, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D12(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}