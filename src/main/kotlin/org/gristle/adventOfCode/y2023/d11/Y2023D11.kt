package org.gristle.adventOfCode.y2023.d11

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.minMax

class Y2023D11(private val image: String) : Day {
    
    private val width = image.indexOf('\n') + 1
    private val height = (image.length + 1) / width
    
    private val xExpansion = (0 until width - 1).filter { x -> 
        (0 until height).all { y -> image[x + y * width] == '.' } 
    }

    private val yExpansion = (0 until height).filter { y ->
        (0 until width - 1).all { x -> image[x + y * width] == '.' }
    }
    
    private val galaxies = buildList { 
        image.forEachIndexed { index, c -> if (c == '#') add(index) }
    }

    private fun shortestPath(a: Int, b: Int, expansionFactor: Int): Long {
        val (minX, maxX) = minMax(a % width, b % width)
        val (minY, maxY) = minMax(a / width, b / width)
        
        val manhattanDistance = (maxX - minX + maxY - minY).toLong()
        val xExpansion = xExpansion.count { it in (minX + 1)..<maxX } * (expansionFactor - 1)
        val yExpansion = yExpansion.count { it in (minY + 1)..<maxY } * (expansionFactor - 1)
        return manhattanDistance + xExpansion + yExpansion
    }
    
    fun solve(expansionFactor: Int): Long = galaxies
        .withIndex()
        .sumOf { (index, a) ->
            galaxies.drop(index + 1).sumOf { b -> shortestPath(a, b, expansionFactor) }
        }

    override fun part1() = solve(2)

    override fun part2() = solve(1_000_000)
}

fun main() = Day.runDay(Y2023D11::class)

//    Class creation: 7ms
//    Part 1: 9545480 (49ms)
//    Part 2: 406725732046 (17ms)
//    Total time: 74ms

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