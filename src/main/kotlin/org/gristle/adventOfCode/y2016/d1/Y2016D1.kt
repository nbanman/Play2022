package org.gristle.adventOfCode.y2016.d1

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2016D1(input: String) {

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

    fun part1(): Int = instructions
        .fold(State(), State::move)
        .manhattanDistance()

    tailrec fun part2(
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
        return part2(
            state.move(instruction),
            visited + newVisited,
            instructions.drop(1)
        )
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D1(readRawInput("y2016/d1"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 226
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 79
}