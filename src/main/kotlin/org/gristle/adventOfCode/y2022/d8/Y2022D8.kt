package org.gristle.adventOfCode.y2022.d8

import org.gristle.adventOfCode.utilities.*

class Y2022D8(input: String) {

    private val forest = input.toGrid()

    private val Coord.treeHeight: Char get() = forest[this]

    private val Coord.isInForest: Boolean get() = forest.validCoord(this)

    private fun isVisible(pos: Coord) = Nsew.values().any { slope ->
        generateSequence(pos.move(slope)) { it.move(slope) }
            .takeWhile { it.isInForest }
            .firstOrNull { it.treeHeight >= pos.treeHeight }
            ?.let { false }
            ?: true
    }

    private fun scenicScore(pos: Coord) = Nsew.values().map { slope ->
        generateSequence(pos.move(slope)) { it.move(slope) }
            .withIndex()
            .first { (_, newPos) -> !newPos.isInForest || newPos.treeHeight >= pos.treeHeight }
            .let { (index, newPos) -> index + if (newPos.isInForest) 1 else 0 }
    }.reduce(Int::times)

    fun part1(): Int = forest.coords().count { pos -> isVisible(pos) }

    fun part2(): Int = forest.coords().maxOf { pos -> scenicScore(pos) }
}

fun main() {
    val input = listOf(
        getInput(8, 2022),
        """30373
25512
65332
33549
35390""",
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D8(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 1708
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 504000
    println("Total time: ${timer.elapsed()}ms")
}