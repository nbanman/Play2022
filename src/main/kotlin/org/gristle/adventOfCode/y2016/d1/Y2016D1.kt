package org.gristle.adventOfCode.y2016.d1

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew

class Y2016D1(input: String) : Day {

    data class Instruction(val turn: Char, val distance: Int)

    data class State(val dir: Nsew = Nsew.NORTH, val coord: Coord = Coord.ORIGIN) {
        fun move(instruction: Instruction): State = when (instruction.turn) {
            'L' -> State(dir.left(), coord.move(dir.left(), instruction.distance))
            else -> State(dir.right(), coord.move(dir.right(), instruction.distance))
        }

        fun manhattanDistance() = coord.manhattanDistance()
    }

    val instructions = input
        .split(", ")
        .map { Instruction(it[0], it.drop(1).toInt()) }

    override fun part1(): Int = instructions
        .fold(State(), State::move)
        .manhattanDistance()

    private tailrec fun solvePart2(
        state: State = State(),
        visited: Set<Coord> = setOf(Coord.ORIGIN),
        instructions: List<Instruction> = this.instructions
    ): Int {
        val instruction = instructions.first()
        val newDir = if (instruction.turn == 'L') state.dir.left() else state.dir.right()
        val newVisited = mutableSetOf<Coord>()
        for (i in 1..instruction.distance) {
            val newPos = state.coord.move(newDir, i)
            if (visited.contains(newPos)) return newPos.manhattanDistance() else newVisited.add(newPos)
        }
        return solvePart2(
            state.move(instruction),
            visited + newVisited,
            instructions.drop(1)
        )
    }

    override fun part2() = solvePart2()
}

fun main() = Day.runDay(Y2016D1::class)

//    Class creation: 6ms
//    Part 1: 226 (3ms)
//    Part 2: 79 (4ms)
//    Total time: 14ms