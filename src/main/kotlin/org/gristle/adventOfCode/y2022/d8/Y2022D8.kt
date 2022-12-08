package org.gristle.adventOfCode.y2022.d8

import org.gristle.adventOfCode.utilities.*

class Y2022D8(input: String) {

    // representation of the heights of all the trees in the forest. Don't mistake these two variables for each other!
    private val forest = input.toGrid()
    private val trees = forest.coords()

    // utility functions that make the coordinates aware of their tree height and whether they are in the forest
    private val Coord.treeHeight: Char get() = forest[this]
    private val Coord.outOfForest: Boolean get() = !forest.validCoord(this)

    // for a given position and Nsew direction, generate coordinates radiating away from the position 
    private fun slopeSequence(tree: Coord, slope: Nsew) =
        generateSequence(tree.move(slope)) { it.move(slope) }

    // determines whether a sequence should be terminated, returning true if the position is out of the forest 
    // or if the tree at the position blocks the treehouse line of sight (LOS).
    private fun terminating(pos: Coord, tree: Coord): Boolean =
        pos.outOfForest || pos.treeHeight >= tree.treeHeight

    // for a given position, checks all directions and returns true if *any* allow LOS out of the forest
    private fun Coord.isVisible(): Boolean {
        val tree = this // unnecessary but provides semantic value
        return Nsew.values().any { slope ->
            slopeSequence(tree, slope)
                // keep delivering coordinates until out of forest or LOS blocked
                .first { pos -> terminating(pos, tree) }
                .outOfForest // true if made it out of forest before LOS blocked
        }
    }

    // for a given position, counts number of visible trees in each direction, then multiplies them together
    private fun scenicScore(treehouse: Coord) = Nsew.values()
        .map { slope ->
            slopeSequence(treehouse, slope)
                // need to track both the position and the index because the index will be used to count the trees
                // and the position will be used to add 1 if LOS is blocked (since a blocking tree 
                // should be counted).    
                .withIndex()
                .first { (_, pos) -> terminating(pos, treehouse) }
                .let { (index, pos) -> index + if (pos.outOfForest) 0 else 1 }
        }.reduce(Int::times)

    fun part1(): Int = trees.count { tree -> tree.isVisible() }

    fun part2(): Int = trees.maxOf { treehouse -> scenicScore(treehouse) }
}

fun main() {
    val input = getInput(8, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D8(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 1708
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 504000
    println("Total time: ${timer.elapsed()}ms")
}