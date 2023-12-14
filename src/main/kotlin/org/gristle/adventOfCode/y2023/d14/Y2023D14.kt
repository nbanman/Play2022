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
                    for (i in index downTo 0 step rocks.width) {
                        val next = i - rocks.width
                        if (next < 0 || tilted[next] in "#O") {
                            tilted[i] = 'O'
                            break
                        }
                    }
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
        val firstIndexOfCycle = generateSequence(initial, ::spinCycle)
            .first { rocks -> !rockFormations.add(rocks) }
            .let { rockFormations.indexOf(it) }
        val cycleSpace = 1_000_000_000 - firstIndexOfCycle
        val cycleLength = rockFormations.size - firstIndexOfCycle
        val answer = rockFormations.elementAt(firstIndexOfCycle + cycleSpace % cycleLength)
        return answer.load()
    }
}

fun main() = Day.runDay(Y2023D14::class)

//    Class creation: 8ms
//    Part 1: 106990 (6ms)
//    Part 2: 100531 (230ms)
//    Total time: 246ms

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