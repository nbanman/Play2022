package org.gristle.adventOfCode.y2021.d11

import org.gristle.adventOfCode.utilities.*

object Y2021D11 {
    private val input = readRawInput("y2021/d11")

    private val grid = input.toGrid().mapToGrid { Character.getNumericValue(it) }
    
    private fun Grid<Int>.stepSequence(): Sequence<Int> = sequence {
        // cave is mutable state internal to the sequence. The grid will keep updating each iteration.
        val cave = toMutableGrid()
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
                    .filter { cave[it] != 0 }
                    .forEach { cave[it] = cave[it] + 1 }
            } while (flasherIndices.isNotEmpty()) // run the inner loop until no more octopuses flash
            yield(cave.count { it == 0 }) // emit the number of octopuses that flashed this turn
        } while (true)
    }
    
    fun part1() = grid.stepSequence().take(100).sum() // sum up all the flashes that occurred in 1st 100 turns

    // look for the first turn where every octopus has flashed (ie, been reset). Get the index of that turn and 
    // add one to compensate for zero-indexing.
    fun part2() = grid.stepSequence().indexOfFirst { it == grid.size } + 1
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D11.part1()} (${elapsedTime(time)}ms)") // 1669
    time = System.nanoTime()
    println("Part 2: ${Y2021D11.part2()} (${elapsedTime(time)}ms)") // 351
}