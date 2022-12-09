package org.gristle.adventOfCode.y2022.d9

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import kotlin.math.abs
import kotlin.math.sign

class Y2022D9(input: String) {

    // parse directional Char to a direction object that can move a Coord in that direction
    private fun Char.toDirection() = when (this) {
        'R' -> Nsew.EAST
        'U' -> Nsew.NORTH
        'L' -> Nsew.WEST
        'D' -> Nsew.SOUTH
        else -> throw IllegalArgumentException("Invalid input")
    }

    // list of positions that the head occupies, including any repeats
    private val headPositions: List<Coord> = input
        .lines() // split into lines
        .flatMap { line -> // parse into a List of directions, expanded so that "U 4" becomes 4 Nsew.NORTH entries
            val (dir, times) = line.split(" ")
            List(times.toInt()) { dir[0].toDirection() }
        }
        // take directions and turn them into a List of positions that the head visits
        .runningFold(Coord.ORIGIN) { pos, direction -> pos.move(direction) }

    // from a list of positions from the link ahead, provide a list of positions that a following link takes
    private fun followPath(frontPositions: List<Coord>): List<Coord> = frontPositions
        // first position is always Origin and the runningFold is seeded with an Origin, so dropping keeps the lists
        // synced
        .drop(1)
        .runningFold(Coord.ORIGIN) { pos, frontPos ->
            val diff = frontPos - pos
            // only move if the link in front is at least 2 away on either the x- or the y-axis
            if (abs(diff.x) == 2 || abs(diff.y) == 2) {
                // move one toward the link in front, on both axes
                Coord(pos.x + diff.x.sign, pos.y + diff.y.sign)
            } else {
                pos // no change 
            }
        }

    fun solve(links: Int): Int = generateSequence(headPositions) { followPath(it) }
        .take(links)
        .last()
        .distinct()
        .size

    fun part1() = solve(2)
    fun part2() = solve(10)
}

fun main() {
    val input = listOf(
        getInput(9, 2022),
        """R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2""",
        """R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20"""
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D9(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 6175
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 2578
    println("Total time: ${timer.elapsed()}ms")
}