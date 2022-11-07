package org.gristle.adventOfCode.y2020.d11

import org.gristle.adventOfCode.utilities.*

class Y2020D11(input: String) {
    /**
     * Represents the states of each potential seat.
     */
    enum class Seat {
        OCCUPIED, UNOCCUPIED, EMPTY_SPACE;

        // Factory method
        companion object {
            fun of(char: Char): Seat {
                return when (char) {
                    '#' -> OCCUPIED
                    'L' -> UNOCCUPIED
                    else -> EMPTY_SPACE
                }
            }
        }

        // Convenience functions for semantic clarity.
        fun isOccupied() = this == OCCUPIED
        fun isEmptySpace() = this == EMPTY_SPACE
    }

    // Parse initial seating layout
    private val layout = input.toGrid().mapToGrid(Seat::of)

    /**
     * Solves both parts using different tolerance levels and different algorithms to determine seat occupation
     * depending on the requirements for the two parts.
     */
    private fun solve(tolerance: Int, getNeighbors: (Grid<Seat>, Int) -> Int): Int {
        // Create a sequence that starts with the original layout and provides successive seat layouts, one per turn
        // The sequence uses the part-specific tolerance parameter and getNeighbor function to generate new states.
        val newStateSequence = generateSequence(layout) {
            it.mapToGridIndexed { index, seat -> // create new Grid
                if (seat.isEmptySpace()) { // empty floor space stays empty
                    Seat.EMPTY_SPACE
                } else {
                    val isOccupied = seat.isOccupied() // looks at whether the current seat is occupied
                    val neighbors = getNeighbors(it, index) // finds number of occupied seats using part-specific algo
                    // uses tolerance parameter to determine whether the seat becomes occupied or unoccupied
                    if ((isOccupied && neighbors < tolerance) || (!isOccupied && neighbors == 0)) {
                        Seat.OCCUPIED
                    } else {
                        Seat.UNOCCUPIED
                    }
                }
            }
        }

        // take the sequence; pair them up with the next iteration; find the first time where the next iteration 
        // does not change (i.e., the pattern has stabilized); then count the number of occupied seats.
        return newStateSequence
            .stabilized()
            .count(Seat::isOccupied)
    }

    fun part1(): Int {
        // pt1 algo for finding occupied neighbors looks at seats immediately adjacent and counts those occupied 
        val getNeighbors = { grid: Grid<Seat>, index: Int ->
            grid.getNeighbors(index, true).count { seat -> seat.isOccupied() }
        }
        return solve(4, getNeighbors)
    }

    fun part2(): Int {
        // pt2 algo for finding occupied neighbors, looks in all directions and returns the number of directions
        // where an occupied seat is found before an unoccupied seat.
        val getNeighbors = { grid: Grid<Seat>, index: Int ->
            // get the starting coordinate
            val startingCoord = grid.coordOf(index)
            // get 8 coordinates representing the slopes to calculate movement along the 8 directions from the
            // starting coordinate
            val slopes = (-1..1).flatMap { y -> (-1..1).map { x -> Coord(x, y) } } - Coord.ORIGIN
            slopes // for each direction...
                .map { slope -> // ...create a sequence of seats extending from line of sight, looking to see if an 
                    // occupied seat is encountered before an unoccupied seat.
                    // If it sees an unoccupied seat, the sequence ends without yielding a value because there is no 
                    // possibility that there will be an occupied seat in that direction per the part 2 rules.
                    sequence {
                        var newCoord = startingCoord + slope // mutable internal state: the next seat in line of sight
                        // yields values until every seat in that direction has been examined, or finds unoccupied seat 
                        while (grid.validCoord(newCoord)) {
                            if (grid[newCoord] == Seat.UNOCCUPIED) {
                                break
                            } else {
                                yield(grid[newCoord])
                            }
                            newCoord += slope // move to next seat along slope
                        }
                    }
                    // count the # directions where traversing the sequence finds an occupied seat.
                }
                .count { lookSequence -> lookSequence.any { seat -> seat.isOccupied() } } // count number of thus occupied directions
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