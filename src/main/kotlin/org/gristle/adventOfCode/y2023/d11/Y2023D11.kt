package org.gristle.adventOfCode.y2023.d11

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.getPairs
import org.gristle.adventOfCode.utilities.minMax
import org.gristle.adventOfCode.utilities.toGrid

class Y2023D11(input: String) : Day {

    private val image = input.toGrid { it == '#' }
    
    private val xExpansion = image.xIndices.filter { x -> image.columns()[x].all { !it } }
    private val yExpansion = image.yIndices.filter { y -> image.rows()[y].all { !it } }
    private val pairs: List<Pair<Coord, Coord>> = image.coords().filter { image[it] }.getPairs()

    private fun shortestPath(a: Coord, b: Coord, expansionFactor: Int): Long {
        val (xMin, xMax) = minMax(a.x, b.x)
        val xRange = xMin..xMax
        val (yMin, yMax) = minMax(a.y, b.y)
        val yRange = yMin..yMax
        val manhattanDistance = xMax.toLong() - xMin + yMax - yMin
        val xExpansion = xExpansion.count { it in xRange } * (expansionFactor - 1)
        val yExpansion = yExpansion.count { it in yRange } * (expansionFactor - 1)
        return manhattanDistance + xExpansion + yExpansion
    }
    
    fun solve(expansionFactor: Int): Long = pairs.sumOf { (a, b) -> shortestPath(a, b, expansionFactor) }

    override fun part1() = solve(2)

    override fun part2() = solve(1_000_000)
}

fun main() = Day.runDay(Y2023D11::class)

//    Class creation: 89ms
//    Part 1: 9545480 (53ms)
//    Part 2: 406725732046 (15ms)
//    Total time: 158ms

@Suppress("unused")
private val sampleInput = listOf(
    """...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
""",
)