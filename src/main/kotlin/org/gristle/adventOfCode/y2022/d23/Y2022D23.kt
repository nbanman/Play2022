package org.gristle.adventOfCode.y2022.d23

import org.gristle.adventOfCode.utilities.*

typealias Grove = MutableGrid<Boolean>

class Y2022D23(input: String) {

    enum class Direction {
        N, S, W, E;

        fun advance(n: Int): Direction = values()[(ordinal + n) % 4]
    }

    private val grove: Grove = input.toMutableGrid { it == '#' }

    // xLimit and yLimit look for the first and last instance of an elf in a given row
    private fun Grove.xLimit(col: Int, direction: Int): Int {
        var x = col
        do {
            for (y in 0 until height) {
                if (this[y * width + x]) return x
            }
            x += direction
        } while (if (direction > 0) x < width else x >= 0)
        return -1
    }

    private fun Grove.yLimit(row: Int, direction: Int): Int {
        var y = row
        do {
            for (x in 0 until width) {
                if (this[y * width + x]) return y
            }
            y += direction
        } while (if (direction > 0) y < height else y >= 0)
        return -1
    }

    // Checks to see if an elf has any neighbors.
    private fun Coord.adjacentEmpty(grove: Grove, dir: Direction): Boolean = when (dir) {
        Direction.N -> listOf(Coord(x - 1, y - 1), copy(y = y - 1), Coord(x + 1, y - 1))
        Direction.S -> listOf(Coord(x - 1, y + 1), copy(y = y + 1), Coord(x + 1, y + 1))
        Direction.W -> listOf(Coord(x - 1, y - 1), copy(x = x - 1), Coord(x - 1, y + 1))
        Direction.E -> listOf(Coord(x + 1, y - 1), copy(x = x + 1), Coord(x + 1, y + 1))
    }.all { !grove.validCoord(it) || !grove[it] }

    // Checks to see if elf has neighbors in a given direction.
    private fun Coord.adjacentEmpty(grove: Grove): Boolean = listOf(
        Coord(x - 1, y - 1), copy(y = y - 1), Coord(x + 1, y - 1),
        copy(x = x - 1), copy(x = x + 1),
        Coord(x - 1, y + 1), copy(y = y + 1), Coord(x + 1, y + 1),
    ).all { !grove.validCoord(it) || !grove[it] }

    // Moves an elf in a particular direction. Using the Coord native move function makes the program 2.5x slower!  
    private fun Coord.moveElf(dir: Direction): Coord = when (dir) {
        Direction.N -> copy(y = y - 1)
        Direction.S -> copy(y = y + 1)
        Direction.W -> copy(x = x - 1)
        Direction.E -> copy(x = x + 1)
    }

    // The meat of the program. Accepts a Grove and outputs a new Grove with the elves moved.
    private fun Grove.move(dir: Direction): Grove {

        // The boundaries of the elves move. To keep the grid the right size, find the bounds of the grove
        // then make a new grid with a padding of 1 around the boundary.
        val xMin = xLimit(0, 1)
        val xMax = xLimit(width - 1, -1)
        val yMin = yLimit(0, 1)
        val yMax = yLimit(height - 1, -1)

        val nextGrove: Grove = MutableGrid(xMax - xMin + 3, yMax - yMin + 3) { false }

        // used to map the new grove's coordinates to the old one
        val offset = Coord(1 - xMin, 1 - yMin)

        // For each elf, try to make a move. Moves are greedy in that if a space is open, an elf will take it.
        // If a later elf thinks that's a good spot too, the spot in the new grove is checked. If an elf has taken
        // that spot, the current elf stays in place and the old elf moves back.
        coords() // get all positions of grove as a Coord
            .filter { this[it] } // only look at the Coords with elves 
            .forEach { pos -> // pos is the position of the elf in the previous grove
                val offsetPos = pos + offset // offsetPos is where the elf would be in nextGrove

                if (pos.adjacentEmpty(this)) { // if no other elves adjacent...
                    nextGrove[offsetPos] = true // stay in place
                } else {
                    (0..3).firstOrNull { i -> // try to move in each direction...
                        val currDir = dir.advance(i)
                        if (pos.adjacentEmpty(this, currDir)) { // if no elves in that dir...
                            // check to see if another elf has already moved there
                            val prospect = offsetPos.moveElf(currDir)
                            if (nextGrove[prospect]) { // if elf already exists
                                nextGrove[offsetPos] = true // stay in place
                                nextGrove[prospect] = false // remove previous move
                                nextGrove[prospect.moveElf(currDir)] = true // put previous elf back in place
                            } else { // ...else move the elf there!
                                nextGrove[prospect] = true
                            }
                            true
                        } else if (i == 3) { // if we've reached the final direction to check... 
                            nextGrove[offsetPos] = true // stay in place
                            true
                        } else { // if we haven't yet reached the final direction to check, we want to keep going 
                            false
                        }
                    } ?: let { // if above returns false, no move possible so stay in place 
                        nextGrove[offsetPos] = true
                    }
                }
            }

        return nextGrove
    }

    // Sequence delivering subsequent versions of the grove, with shifting winds.
    private val movement =
        generateSequence(grove to Direction.N) { (current, dir) -> current.move(dir) to dir.advance(1) }

    fun part1(): Int = movement
        .take(11) // iterate 10 times from initial
        .last() // we just want the last one
        .let { (grove, _) ->
            // find the width and height of the box if there were no padding left
            val width = grove.xLimit(grove.width - 1, -1) - grove.xLimit(0, 1) + 1
            val height = grove.yLimit(grove.height - 1, -1) - grove.yLimit(0, 1) + 1
            // the empty spaces are the area of that hypothetical box minus the number of elves
            width * height - grove.count { it }
        }

    fun part2(): Int = movement
        .zipWithNext() // zip each grove with the previous grove
        .indexOfFirst { (prev, next) -> // compare the two groves...
            prev.first == next.first // grab the first time when they are the same
        } + 1 // add one because we actually want the second time they are the same
}

fun main() {
    val input = getInput(23, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D23(input)
    println("Class creation: ${timer.lap()}ms") // 17ms
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 3812 (65ms) (original 10228ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 1003 (697ms) (original 374635ms!!!)
    println("Total time: ${timer.elapsed()}ms") // 780ms
}
