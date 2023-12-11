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
    private val pairs = image.coords().filter { image[it] }.getPairs()

    private fun shortestPath(galaxies: Pair<Coord, Coord>, expansionFactor: Int): Long {
        val (a, b) = galaxies
        val (xMin, xMax) = minMax(a.x, b.x)
        val xRange = xMin..xMax
        val (yMin, yMax) = minMax(a.y, b.y)
        val yRange = yMin..yMax
        val manhattanDistance = a.manhattanDistance(b).toLong()
        val xExpansion = xRange.count { it in xExpansion } * (expansionFactor - 1)
        val yExpansion = yRange.count { it in yExpansion } * (expansionFactor - 1)
        return manhattanDistance + xExpansion + yExpansion
    }
    
    

    override fun part1() = pairs.sumOf { shortestPath(it, 2) }

    override fun part2() = pairs.sumOf { shortestPath(it, 1_000_000) }
}

fun main() = Day.runDay(Y2023D11::class)

//    Class creation: 90ms
//    Part 1: 9545480 (124ms)
//    Part 2: 406725732046 (101ms)
//    Total time: 316ms

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