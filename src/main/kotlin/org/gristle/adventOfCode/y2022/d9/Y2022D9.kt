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

    // take input and turn it into a list of directions to move
    private fun String.toMotions() = lines().flatMap { line ->
        val (dir, times) = line.split(" ")
        List(times.toInt()) { dir[0].toDirection() }
    }

    // list of positions that the head occupies, including any repeats
    private val headPositions: List<Coord> = buildList {
        add(Coord.ORIGIN)
        for (direction in input.toMotions()) {
            add(last().move(direction))
        }
    }

    // from a list of positions, provide a list of positions that a following link takes
    private fun followPath(front: List<Coord>): List<Coord> = buildList {
        for (head in front.drop(1)) {
            val previous = lastOrNull() ?: Coord.ORIGIN
            add(previous.follow(head))
        }
    }

    // used in followPath() to have a link calculate its next position depending on where the leading link is
    private fun Coord.follow(head: Coord): Coord {
        val diff = head - this
        return if (abs(diff.x) == 2 || abs(diff.y) == 2) {
            Coord(x + diff.x.sign, y + diff.y.sign)
        } else {
            this // no change
        }
    }

    fun part1() = followPath(headPositions).distinct().size

    fun part2(): Int = generateSequence(headPositions) { followPath(it) }
        .take(10)
        .last()
        .distinct()
        .size
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