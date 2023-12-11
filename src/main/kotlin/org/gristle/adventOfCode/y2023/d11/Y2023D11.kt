package org.gristle.adventOfCode.y2023.d11

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.toGrid
import kotlin.math.abs

class Y2023D11(input: String) : Day {
    private val image: Grid<Boolean> = input.toGrid { it == '#' }

    // for each axis, get indexed iterables of pairs. The first being the index where galaxies reside, the 
    // second being the number of galaxies. We separate the axes to avoid repeat calculation.
    private val xGal = image.xIndices
        .map { x -> x to image.column(x).count { it } }
        .filter { (_, count) -> count > 0 }
        .withIndex()
    private val yGal = image.yIndices
        .map { y -> y to image.row(y).count { it } }
        .filter { (_, count) -> count > 0 }
        .withIndex()

    // for each axis, track the indices representing expansion fields
    private val xExpansions: List<Int> = image.xIndices.filter { x -> image.column(x).none { it } }
    private val yExpansions: List<Int> = image.yIndices.filter { y -> image.row(y).none { it } }

    // run the distance function twice (once for each axis), return the sum 
    fun solve(expansionFactor: Int): Long = distance(expansionFactor, xGal, xExpansions) +
            distance(expansionFactor, yGal, yExpansions)

    // Get the distance between two indices where galaxies reside.
    // This involves calculating the unexpanded difference multiplied by the number of expansions passed times
    // the expansion factor
    // Lastly, the distance is multiplied by #galaxies in the first index multiplied by #galaxies in the second index.
    fun distance(
        expansionFactor: Int,
        galaxies: Iterable<IndexedValue<Pair<Int, Int>>>,
        expansions: List<Int>
    ): Long {
        return galaxies.sumOf { (i, a) ->
            val (aPos, aCount) = a

            // calculate which expansions are to the left of the source galaxies
            // this returns a negative number due to how binarySearch returns values but this will be rectified
            // later.
            val alreadyPassed = expansions.binarySearch(aPos)
            galaxies.drop(i + 1).sumOf { (_, b) ->
                val (bPos, bCount) = b

                // calculates which expansions are to the left of the destination galaxies, and subtracts the
                // ones to the left of the source galaxies. The abs function handles the negative value.
                val expansionsPassed = abs(expansions.binarySearch(bPos) - alreadyPassed)
                ((bPos - aPos) + expansionsPassed.toLong() * (expansionFactor - 1)) * aCount * bCount
            }
        }
    }

    override fun part1() = solve(2)

    override fun part2() = solve(1_000_000)
}

fun main() = Day.runDay(Y2023D11::class)

//    Class creation: 18ms
//    Part 1: 9545480 (12ms)
//    Part 2: 406725732046 (6ms)
//    Total time: 37ms

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