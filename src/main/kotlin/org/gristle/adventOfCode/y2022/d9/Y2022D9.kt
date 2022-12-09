package org.gristle.adventOfCode.y2022.d9

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput

class Y2022D9(input: String) {

    private fun Char.toDirection() = when (this) {
        'R' -> Nsew.EAST
        'U' -> Nsew.NORTH
        'L' -> Nsew.WEST
        'D' -> Nsew.SOUTH
        else -> throw IllegalArgumentException("Invalid input")
    }

    private fun String.toMotions() = lines().flatMap { line ->
        val (dir, times) = line.split(" ")
        List(times.toInt()) { dir[0].toDirection() }
    }

    private val headPositions: List<Coord> = buildList {
        add(Coord.ORIGIN)
        for (direction in input.toMotions()) {
            add(last().move(direction))
        }
    }

    private fun Coord.follow(head: Coord): Coord {
        val adjacentRange = -1..1
        val diff = head - this
        return if (diff.x in adjacentRange && diff.y in adjacentRange) {
            this
        } else {
            Coord(x + diff.x.coerceIn(adjacentRange), y + diff.y.coerceIn(adjacentRange))
        }
    }

    private fun followPath(front: List<Coord>): List<Coord> = buildList {
        add(Coord.ORIGIN)
        for (head in front.drop(1)) {
            add(last().follow(head))
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