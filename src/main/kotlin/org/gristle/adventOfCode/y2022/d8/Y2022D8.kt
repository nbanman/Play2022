package org.gristle.adventOfCode.y2022.d8

import org.gristle.adventOfCode.utilities.*

class Y2022D8(input: String) {

    private val forest = input.toGrid(Char::toDigit)

    private val slopes = listOf(Coord(-1, 0), Coord(0, -1), Coord(1, 0), Coord(0, 1))

    private fun isVisible(pos: Coord): Boolean {

        fun directionVisible(slope: Coord): Boolean {
            return generateSequence(pos + slope) { it + slope }
                .takeWhile { forest.validCoord(it) }
                .firstOrNull { forest[it] >= forest[pos] }
                ?.let { false }
                ?: true
        }

        return slopes.any { slope -> directionVisible(slope) }

    }

    private fun scenicScore(pos: Coord) = slopes
        .map { slope ->
            var newCoord = pos + slope
            var trees = 0
            while (forest.validCoord(newCoord)) {
                trees++
                if (forest[newCoord] >= forest[pos]) break
                newCoord += slope
            }
            trees
        }.reduce(Int::times)

    fun part1(): Int = forest.coords().count { pos ->
        pos.x == 0 || pos.y == 0 || pos.x == forest.xIndices.last
                || pos.y == forest.yIndices.last || isVisible(pos)
    }

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