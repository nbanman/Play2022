package org.gristle.adventOfCode.y2023.d14

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.minMaxRanges
import org.gristle.adventOfCode.utilities.stabilized

class Y2023D14(input: String) : Day {

    private val size: Coord
    private val rocks: Set<Coord>
    private val cubes: Set<Coord>
    
    init {
        val width = input.indexOf('\n') + 1
        val height = (input.length + 1) / width
        size = Coord(width - 1, height)
        rocks = buildSet {
            val rocks = this
            cubes = buildSet { 
                for ((i, v) in input.withIndex()) {
                    if (v in ".\n") continue
                    val pos = Coord(i % width, i / width)
                    if (v == 'O') rocks.add(pos) else add(pos)
                }
            }
        }
    }

    private fun Coord.canMove(rocks: Set<Coord>, dir: Nsew): Boolean = 
        generateSequence(move(dir)) { it.move(dir) }
            .first { it in cubes || it !in rocks }
            .let { it !in cubes && it.x in 0 until size.x && it.y in 0 until size.y }
    
    private fun moveRocks(rocks: Set<Coord>, dir: Nsew): Set<Coord> = buildSet {
        for (rock in rocks) {
            if (rock.canMove(rocks, dir)) {
                val moved = rock.move(dir, 1, this@Y2023D14.size)
                if (moved in this || moved in cubes) {
                    add(rock)
                } else {
                    add(moved)
                }    
            } else {
                add(rock)
            }
        }
    }
    
    private fun moveToSide(rocks: Set<Coord>, dir: Nsew) = generateSequence(rocks) { moveRocks(it, dir) }
        .stabilized()
    
    private fun Coord.load() = size.y - this.y
    
    override fun part1() = moveToSide(rocks, Nsew.NORTH).sumOf { it.load() }

    override fun part2(): Int {
        fun spinCycle(rocks: Set<Coord>) = listOf(Nsew.NORTH, Nsew.WEST, Nsew.SOUTH, Nsew.EAST)
            .fold(rocks, ::moveToSide)
        val rockFormations = mutableSetOf<Set<Coord>>()
        val repeat = generateSequence(rocks, ::spinCycle)
            .first { rocks -> !rockFormations.add(rocks) }
        val repeatFirst = rockFormations.indexOf(repeat)
        val repeatLast = rockFormations.size
        val cycles = 1_000_000_000 - repeatFirst
        val cycleLength = rockFormations.size - repeatFirst
        val answer = rockFormations
            .elementAt(repeatFirst + cycles % cycleLength)
        return answer.sumOf { it.load() }
    }
    
    companion object {
        fun graphicString(rocks: Set<Coord>, cubes: Set<Coord>): String {
            val (xRange, yRange) = (rocks + cubes).minMaxRanges()
            return buildString {
                Coord.forRectangle(xRange, yRange) { coord ->
                    if (coord.x == xRange.first && coord.y != yRange.first) append('\n')
                    append(if (coord in rocks) 'O' else if (coord in cubes) '#' else '.')
                }
                append('\n')
            }
        }
    }
}

fun main() = Day.runDay(Y2023D14::class)

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