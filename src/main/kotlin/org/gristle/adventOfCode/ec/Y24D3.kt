package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.toGrid

fun main() {
    val timer = Stopwatch(true)
    println("1. ${solve(part = 1, expected = 134)}: ${timer.lap()}ms")
    println("2. ${solve(part = 2, expected = 2810)}: ${timer.lap()}ms")
    println("3. ${solve(part = 3, diagonals = true, expected = 10443)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}


private fun solve(part: Int, diagonals: Boolean = false, expected: Int): String {
    val solution = solve(part, diagonals)
    val passFail = if (solution == expected) "PASS" else "FAIL ($expected)"
    return "$solution, $passFail"
}

private fun solve(part: Int, diagonals: Boolean = false): Int {
    val input = inputs[part - 1]
    val blocks = input
        .toGrid()
        .let { grid -> grid.coords().filter { pos -> grid[pos] == '#' }.toSet() }
    val dig = { blocks: Set<Coord> ->
        blocks
            .filter { block -> block.getNeighbors(diagonals).all { it in blocks } }
            .toSet()
    }
    return generateSequence(blocks, dig)
        .takeWhile { it.isNotEmpty() }
        .fold(0) { acc, blocks -> acc + blocks.size }
}

private val inputs: List<String> = ecInputs(24, 3)

private val tests = listOf(
    """..........
..###.##..
...####...
..######..
..######..
...####...
..........""",
)
