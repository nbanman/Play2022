package org.gristle.adventOfCode.y2020.d11

import org.gristle.adventOfCode.utilities.*

class Y2020D11(input: String) {
    // Initial seating layout
    private val layout = input.toGrid()

    /**
     * Solves both parts using different tolerance levels and different algorithms to determine seat occupation
     * depending on the requirements for the two parts.
     */
    private fun solve(tolerance: Int, getNeighbors: (Grid<Char>, Int) -> Int): Int {
        // Create a sequence that starts with the original layout and provides successive seat layouts, one per turn
        // The sequence uses the part-specific tolerance parameter and getNeighbor function to generate new states.
        val newStateSequence = generateSequence(layout) {
            it.mapToGridIndexed { index, seat -> // create new Grid
                if (seat == '.') { // empty floor space stays empty
                    '.'
                } else {
                    val occupied = seat == '#' // looks at whether the current seat is occupied
                    val neighbors = getNeighbors(it, index) // finds number of occupied seats using part-specific algo
                    // uses tolerance parameter to determine whether the seat becomes occupied or unoccupied
                    if ((occupied && neighbors < tolerance) || (!occupied && neighbors == 0)) '#' else 'L'
                }
            }
        }

        // take the sequence; pair them up with the next iteration; find the first time where the next iteration 
        // does not change (i.e., the pattern has stabilized); then count the number of occupied seats.
        return newStateSequence
            .zipWithNext()
            .first { (prev, next) -> prev == next }
            .second
            .count { it == '#' }
    }

    fun part1(): Int {
        // pt1 algo for finding occupied neighbors looks at seats immediately adjacent and counts those occupied 
        val getNeighbors = { grid: Grid<Char>, index: Int ->
            grid.getNeighbors(index, true).count { it == '#' }
        }
        return solve(4, getNeighbors)
    }

    fun part2(): Int {
        // pt2 algo for finding occupied neighbors, looks in all directions and returns true if there are any 
        // occupied seats in line of sight.
        val getNeighbors = { grid: Grid<Char>, index: Int ->
            // get the starting coordinate
            val startingCoord = grid.coordOf(index)
            // get 8 coordinates representing the slopes to calculate movement along the 8 directions from the
            // starting coordinate
            val slopes = (-1..1).flatMap { y -> (-1..1).map { x -> Coord(x, y) } } - Coord.ORIGIN
            slopes // for each slope...
                .map { slope ->
                    // create a Sequence that starts at the starting coordinate, and looks in the direction of the
                    // slope. If it sees an unoccupied seat or there are no more seats in that direction, the
                    // sequence ends without yielding a value. If it sees an occupied seat, it yields true and the 
                    // sequence ends. If it sees empty floor, it yields false and continues.
                    sequence {
                        var newCoord = startingCoord + slope // mutable internal state: the next seat in line of sight
                        // yields values until every seat in that direction has been examined, or early return
                        while (grid.validCoord(newCoord)) {
                            when {
                                grid[newCoord] == 'L' -> break // unoccupied seat found; break
                                grid[newCoord] == '#' -> {
                                    yield(true); break
                                } // occupied seat found; break
                                else -> yield(false) // empty space; keep going
                            }
                            newCoord += slope // move to next seat along slope
                        }
                    }.any { it } // return true if the sequence yields a true value before ending, meaning that there 
                    // was an occupied seat in that direction
                }.count { it } // count number of thus occupied directions
        }
        return solve(5, getNeighbors)
    }
}
fun main() {
    var time = System.nanoTime()
    val c = Y2020D11(readRawInput("y2020/d11"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 2243
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2027
}