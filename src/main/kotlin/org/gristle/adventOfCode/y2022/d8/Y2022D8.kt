package org.gristle.adventOfCode.y2022.d8

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.toGrid

class Y2022D8(input: String) : Day {

    // Representations of the positions and heights of all the trees in the forest. 
    // Don't mistake these two variables for each other!
    private val forest = input.toGrid()
    private val treeHeights = forest.coords().associateWith { forest[it] }

    // utility function that makes the coordinates aware of whether they are in the forest
    private val Coord.outOfForest: Boolean get() = !treeHeights.containsKey(this)

    // for a given position, provide a list of sequences that generate coordinates radiating away from the position
    // in each of the four directions
    private fun rays(tree: Coord): List<Sequence<Coord>> = Nsew.values().map { direction ->
        generateSequence(tree.move(direction)) { it.move(direction) }
    }

    // determines whether a sequence should be terminated, returning true if the position is out of the forest 
    // or if the tree at the position blocks the starting tree's line of sight (LOS).
    private fun terminating(pos: Coord, tree: Coord): Boolean {
        val posHeight = treeHeights[pos] ?: return true
        return posHeight >= treeHeights.getValue(tree)
    }

    // for a given position, checks all directions and returns true if *any* allow LOS out of the forest
    private fun Coord.isVisible(): Boolean {
        val tree = this // unnecessary but provides semantic value
        return rays(tree).any { ray ->
            ray
                // keep delivering coordinates until out of forest or LOS blocked
                .first { pos -> terminating(pos, tree) }
                .outOfForest // true if made it out of forest before LOS blocked
        }
    }

    // for a given position, counts number of visible trees in each direction, then multiplies them together
    private fun scenicScore(treehouse: Coord) = rays(treehouse)
        .map { ray ->
            ray
                // need to track both the position and the index because the index will be used to count the trees
                // and the position will be used to add 1 if LOS is blocked (since a blocking tree 
                // should be counted).    
                .withIndex()
                .first { (_, pos) -> terminating(pos, treehouse) }
                .let { (index, pos) -> index + if (pos.outOfForest) 0 else 1 }
        }.reduce(Int::times)

    override fun part1(): Int = treeHeights.keys.count { tree -> tree.isVisible() }

    override fun part2(): Int = treeHeights.keys.maxOf(::scenicScore)
}

fun main() = Day.runDay(Y2022D8::class) // 1708, 504000