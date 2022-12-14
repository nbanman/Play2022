package org.gristle.adventOfCode.y2022.d14

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.getInts

class Y2022D14(input: String) {

    private val cavern: Set<Coord> = buildSet {
        input.lines().forEach { line ->
            line.getInts()
                .chunked(2) { (x, y) -> Coord(x, y) }
                .zipWithNext()
                .forEach { (prev, next) -> addAll(prev.lineTo(next)) }
        }
    }

    private val depth = cavern.maxOf(Coord::y)

    private fun Coord.fall(cavern: MutableSet<Coord>): Coord? {
        return when {
            !cavern.contains(south()) -> south()
            !cavern.contains(southwest()) -> southwest()
            !cavern.contains(southeast()) -> southeast()
            else -> null
        }
    }

    private fun settle(cavern: MutableSet<Coord>) = generateSequence(Coord(500, 0)) { it.fall(cavern) }
        .takeWhile { it.y <= depth + 1 }
        .last()
        .apply { cavern.add(this) }

    fun part1(): Int {
        val cave = cavern.toMutableSet()
        return generateSequence { settle(cave) }
            .indexOfFirst { it.y > depth }
    }

    fun part2(): Int {
        val cave = cavern.toMutableSet()
        return 1 + generateSequence { settle(cave) }
            .indexOfFirst { it == Coord(500, 0) }
    }
}


fun main() {
    val input = listOf(
        getInput(14, 2022),
        """498,4 -> 498,6 -> 496,6
            |503,4 -> 502,4 -> 502,9 -> 494,9
        """.trimMargin(),
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D14(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 825
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 26729
    println("Total time: ${timer.elapsed()}ms")
}