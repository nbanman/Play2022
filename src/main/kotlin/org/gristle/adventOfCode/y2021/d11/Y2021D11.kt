package org.gristle.adventOfCode.y2021.d11

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid
import org.gristle.adventOfCode.utilities.toMutableGrid

class Y2021D11(input: String) {

    private val grid = input.toGrid(Character::getNumericValue)

    // a Sequence which keeps track of mutating state of the grid through any number of turns, emitting the
    // number of flashes that occurred each turn
    private val flashSequence = sequence {
        // cave is mutable state internal to the sequence. The grid will keep updating each iteration.
        val cave = grid.toMutableGrid()
        // outer loop runs forever. The yield turns this into a generator. Each time, it increases the energy
        // level of each octopus, then runs an inner loop that handles the flashing, then yields the number of
        // flashes
        do {
            cave.indices.forEach { index -> cave[index] = cave[index] + 1 } // increases energy level 
            do { // inner loop to handle flashing
                // gets indices of all octopuses ready to flash
                val flasherIndices = cave.withIndex().filter { it.value > 9 }.map { it.index } 

                // for each flashing octopus, reset energy to 0
                flasherIndices.forEach { index -> cave[index] = 0 }
                
                // for each flashing octopus, get its neighbors, filter out those that have already flashed,
                // and add one to the energy
                flasherIndices
                    .flatMap { cave.getNeighborIndices(it, true) }
                    .filterNot { cave[it] == 0 }
                    .forEach { cave[it] = cave[it] + 1 }
            } while (flasherIndices.isNotEmpty()) // run the inner loop until no more octopuses flash
            yield(cave.count { it == 0 }) // emit the number of octopuses that flashed this turn
        } while (true)
    }

    fun part1() = flashSequence.take(100).sum() // sum up all the flashes that occurred in 1st 100 turns

    // look for the first turn where every octopus has flashed (ie, been reset). Get the index of that turn and 
    // add one to compensate for zero-indexing.
    fun part2() = flashSequence.indexOfFirst { it == grid.size } + 1
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2021D11(readRawInput("y2021/d11"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 1669
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 351
    println("Total time: ${timer.elapsed()}ms")
}