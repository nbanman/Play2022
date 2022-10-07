package org.gristle.adventOfCode.y2020.d12

import org.gristle.adventOfCode.utilities.*

class Y2020D12(input: String) {

    // Holds instruction info
    data class Instruction(val action: Char, val amount: Int)

    // State interface allows use of two different State objects with different values/functions to work with the 
    // solve function.
    interface State {
        val pos: Coord
        fun executeInstruction(instruction: Instruction): State
    }

    // State used for part 1. Holds the current position and direction. 
    data class DirState(override val pos: Coord = Coord.ORIGIN, private val dir: Nsew = Nsew.EAST) : State {
        // Provides a new state based on part 1 execution of instructions.
        override fun executeInstruction(instruction: Instruction) = when (instruction.action) {
            'N' -> copy(pos = pos.north(instruction.amount))
            'S' -> copy(pos = pos.south(instruction.amount))
            'E' -> copy(pos = pos.east(instruction.amount))
            'W' -> copy(pos = pos.west(instruction.amount))
            'L' -> copy(dir = dir.left(instruction.amount / 90))
            'R' -> copy(dir = dir.right(instruction.amount / 90))
            else -> copy(pos = pos.move(dir, instruction.amount))
        }
    }

    // State used for part 2. Holds the current position and waypoint coordinates.
    data class WaypointState(
        override val pos: Coord = Coord.ORIGIN,
        private val waypoint: Coord = Coord(10, -1)
    ) : State {
        // Provides a new state based on part 2 execution of instructions.
        override fun executeInstruction(instruction: Instruction) = when (instruction.action) {
            'N' -> copy(waypoint = waypoint.north(instruction.amount))
            'S' -> copy(waypoint = waypoint.south(instruction.amount))
            'E' -> copy(waypoint = waypoint.east(instruction.amount))
            'W' -> copy(waypoint = waypoint.west(instruction.amount))
            'L' -> copy(waypoint = (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(acc.y, -acc.x) })
            'R' -> copy(waypoint = (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(-acc.y, acc.x) })
            else -> copy(pos = (1..instruction.amount).fold(pos) { acc, _ -> acc + waypoint })
        }
    }

    // parses input into list of Instructions
    private val instructions = input.lines().map { Instruction(it.first(), it.drop(1).toInt()) }

    // for both parts, start with initial state, execute instructions on each successive state, then take the
    // final location and find the distance from the origin.
    private fun solve(initialState: State) = instructions
        .fold(initialState, State::executeInstruction)
        .pos
        .manhattanDistance()

    fun part1() = solve(DirState())

    fun part2() = solve((WaypointState()))
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D12(readRawInput("y2020/d12"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 2280
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 38693
}