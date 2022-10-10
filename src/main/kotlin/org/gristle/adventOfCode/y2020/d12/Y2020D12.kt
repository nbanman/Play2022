package org.gristle.adventOfCode.y2020.d12

import org.gristle.adventOfCode.utilities.*

class Y2020D12(input: String) {

    // Holds instruction info
    data class Instruction(val action: Char, val amount: Int)

    // State interface allows use of two different State objects with different values/functions to work with the 
    // solve function.
    interface ShipState {
        fun nextState(instruction: Instruction): ShipState

        fun manhattanDistance(): Int
    }

    // State used for part 1. Holds the current position and direction. 
    data class DirShipState(val pos: Coord = Coord.ORIGIN, val dir: Nsew = Nsew.EAST) : ShipState {
        // Provides a new state based on part 1 execution of instructions.
        override fun nextState(instruction: Instruction): DirShipState = when (instruction.action) {
            'N' -> copy(pos = pos.north(instruction.amount))
            'S' -> copy(pos = pos.south(instruction.amount))
            'E' -> copy(pos = pos.east(instruction.amount))
            'W' -> copy(pos = pos.west(instruction.amount))
            'L' -> copy(dir = dir.left(instruction.amount / 90))
            'R' -> copy(dir = dir.right(instruction.amount / 90))
            else -> copy(pos = pos.move(dir, instruction.amount))
        }

        override fun manhattanDistance(): Int = pos.manhattanDistance()
    }

    // State used for part 2. Holds the current position and waypoint coordinates.
    data class WaypointShipState(
        val pos: Coord = Coord.ORIGIN,
        val waypoint: Coord = Coord(10, -1)
    ) : ShipState {
        // Provides a new state based on part 2 execution of instructions.
        override fun nextState(instruction: Instruction): WaypointShipState = when (instruction.action) {
            'N' -> copy(waypoint = waypoint.north(instruction.amount))
            'S' -> copy(waypoint = waypoint.south(instruction.amount))
            'E' -> copy(waypoint = waypoint.east(instruction.amount))
            'W' -> copy(waypoint = waypoint.west(instruction.amount))
            'L' -> copy(waypoint = (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(acc.y, -acc.x) })
            'R' -> copy(waypoint = (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(-acc.y, acc.x) })
            else -> copy(pos = (1..instruction.amount).fold(pos) { acc, _ -> acc + waypoint })
        }

        override fun manhattanDistance(): Int = pos.manhattanDistance()
    }

    // parses input into list of Instructions
    private val instructions = input.lines().map { Instruction(it.first(), it.drop(1).toInt()) }

    // for both parts, start with initial state, execute instructions on each successive state, then take the
    // final location and find the distance from the origin.
    private fun solve(initialShipState: ShipState) =
        instructions
            .fold(initialShipState, ShipState::nextState)
            .manhattanDistance()

    fun part1() = solve(DirShipState())

    fun part2() = solve((WaypointShipState()))
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