package org.gristle.adventOfCode.y2020.d12

import org.gristle.adventOfCode.utilities.*

class Y2020D12(input: String) {

    // Holds instruction info
    data class Instruction(val action: Char, val amount: Int)

    // Interface allows use of two different State objects with different values/functions to work with the 
    // solve function.
    interface ShipState {
        val pos: Coord
        fun manhattanDistance() = pos.manhattanDistance()
        fun nextState(instruction: Instruction): ShipState
    }

    // State used for part 1. Holds the current position and direction. 
    data class DirShipState(override val pos: Coord, val dir: Nsew) : ShipState {
        // Provides a new state based on part 1 execution of instructions.
        override fun nextState(instruction: Instruction) = when (instruction.action) {
            'N' -> copy(pos = pos.north(instruction.amount))
            'S' -> copy(pos = pos.south(instruction.amount))
            'E' -> copy(pos = pos.east(instruction.amount))
            'W' -> copy(pos = pos.west(instruction.amount))
            'L' -> copy(dir = dir.left(instruction.amount / 90))
            'R' -> copy(dir = dir.right(instruction.amount / 90))
            'F' -> copy(pos = pos.move(dir, instruction.amount))
            else -> throw IllegalArgumentException("Invalid Instruction: $instruction")
        }
    }

    // State used for part 2. Holds the current position and waypoint coordinates.
    data class WaypointShipState(override val pos: Coord, val waypoint: Coord) : ShipState {
        // Provides a new state based on part 2 execution of instructions.
        override fun nextState(instruction: Instruction) = when (instruction.action) {
            'N' -> copy(waypoint = waypoint.north(instruction.amount))
            'S' -> copy(waypoint = waypoint.south(instruction.amount))
            'E' -> copy(waypoint = waypoint.east(instruction.amount))
            'W' -> copy(waypoint = waypoint.west(instruction.amount))
            'L' -> copy(waypoint = (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(acc.y, -acc.x) })
            'R' -> copy(waypoint = (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(-acc.y, acc.x) })
            'F' -> copy(pos = (1..instruction.amount).fold(pos) { acc, _ -> acc + waypoint })
            else -> throw IllegalArgumentException("Invalid Instruction: $instruction")
        }
    }

    // parses input into list of Instructions
    private val instructions: List<Instruction> = input
        .lines()
        .map { Instruction(it.first(), it.drop(1).toInt()) }

    // for both parts, start with initial state, execute instructions on each successive state, then take the
    // final location and find the distance from the origin.
    private fun solve(initialShipState: ShipState): Int = instructions
        .fold(initialShipState, ShipState::nextState)
        .manhattanDistance()

    fun part1() = solve(DirShipState(Coord.ORIGIN, Nsew.EAST))

    fun part2() = solve(WaypointShipState(Coord.ORIGIN, Coord(10, -1)))
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