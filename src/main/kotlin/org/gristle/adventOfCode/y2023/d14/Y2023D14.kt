package org.gristle.adventOfCode.y2023.d14

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.MutableGrid
import org.gristle.adventOfCode.utilities.toGrid

class Y2023D14(input: String) : Day {

    private val initial = input.toGrid()

    private fun tiltUp(rocks: Grid<Char>): Grid<Char> {
        val tilted = MutableGrid(rocks.width, rocks.height) { '.' }
        rocks.forEachIndexed { index, c ->
            when (c) {
                '#' -> tilted[index] = '#'
                'O' -> {
                    val tiltedIndex = generateSequence(index) { it - rocks.width }
                        .takeWhile { it >= 0 && tilted[it] !in "#O" }
                        .last()
                    tilted[tiltedIndex] = 'O'
                }
            }
        }
        return tilted
    }

    private fun Grid<Char>.load() = withIndex().sumOf { (index, c) ->
        if (c == 'O') height - index / width else 0
    }

    override fun part1(): Int = tiltUp(initial).load()

    override fun part2(): Int {
        fun spinCycle(rocks: Grid<Char>): Grid<Char> = (1..4).fold(rocks) { acc, i ->
            tiltUp(acc).rotate90().toGrid(acc.height)
        }
        val rockFormations = mutableSetOf<Grid<Char>>()
        val repeat = generateSequence(initial, ::spinCycle)
            .first { rocks -> !rockFormations.add(rocks) }
        val repeatFirst = rockFormations.indexOf(repeat)
        val cycles = 1_000_000_000 - repeatFirst
        val cycleLength = rockFormations.size - repeatFirst
        val answer = rockFormations
            .elementAt(repeatFirst + cycles % cycleLength)
        return answer.load()
    }
}

fun main() = Day.runDay(Y2023D14::class)

//    Class creation: 9ms
//    Part 1: 106990 (11ms)
//    Part 2: 100531 (350ms)
//    Total time: 371ms

@Suppress("unused")
private val sampleInput = listOf(
    """O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
""",
)