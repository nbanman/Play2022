package org.gristle.adventOfCode.y2023.d11

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.minMax

class Y2023D11(private val image: String) : Day {
    
    // working with string directly, so need width and height of grid in order to traverse up/down/left/right
    private val width: Int = image.indexOf('\n') + 1
    
    // note that it is image.length '+ 1' because my input has trim() called on it before getting to me, getting
    // rid of the last '\n'
    private val height: Int = (image.length + 1) / width
    
    // goes row by row (then column by column), keeping only those where there are no galaxies
    private val xExpansion: List<Int> = (0 until width - 1).filter { x -> 
        (0 until height).all { y -> image[x + y * width] == '.' } 
    }
    private val yExpansion: List<Int> = (0 until height).filter { y ->
        (0 until width - 1).all { x -> image[x + y * width] == '.' }
    }
    
    // list o'galaxies
    private val galaxies: List<Int> = buildList { 
        image.forEachIndexed { index, c -> if (c == '#') add(index) }
    }

    private fun shortestPath(a: Int, b: Int, expansionFactor: Int): Long {
        // negative ranges don't work, so we need to know which is largest and smallest on each axis
        val (minX, maxX) = minMax(a % width, b % width)
        val (minY, maxY) = minMax(a / width, b / width)
        
        // get distance w/o taking expansion into account
        val manhattanDistance = (maxX - minX + maxY - minY).toLong()
        
        // for every expansion row and column, add additional distance
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