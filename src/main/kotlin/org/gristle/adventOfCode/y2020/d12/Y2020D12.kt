package org.gristle.adventOfCode.y2020.d12

import org.gristle.adventOfCode.utilities.*

class Y2020D12(input: String) {

    // Holds instruction info
    data class Instruction(val action: Char, val amount: Int)

    interface State {
        val pos: Coord
        fun executeInstruction(instruction: Instruction): State
    }

    // State used for part 1. Holds the current position and direction. 
    class DirState(override val pos: Coord = Coord.ORIGIN, private val dir: Nsew = Nsew.EAST) : State {
        // Provides a new state based on part 1 parsing of instructions.
        override fun executeInstruction(instruction: Instruction): DirState {
            return when (instruction.action) {
                'N' -> DirState(pos.north(instruction.amount), dir)
                'S' -> DirState(pos.south(instruction.amount), dir)
                'E' -> DirState(pos.east(instruction.amount), dir)
                'W' -> DirState(pos.west(instruction.amount), dir)
                'L' -> DirState(pos, dir.left(instruction.amount / 90))
                'R' -> DirState(pos, dir.right(instruction.amount / 90))
                else -> DirState(pos.move(dir, instruction.amount), dir)
            }
        }
    }

    // State used for part 2. Holds the current position and waypoint coordinates.
    class WaypointState(
        override val pos: Coord = Coord.ORIGIN,
        private val waypoint: Coord = Coord(10, -1)
    ) : State {
        // Provides a new state based on part 2 parsing of instructions.
        override fun executeInstruction(instruction: Instruction): WaypointState {
            return when (instruction.action) {
                'N' -> WaypointState(pos, waypoint.north(instruction.amount))
                'S' -> WaypointState(pos, waypoint.south(instruction.amount))
                'E' -> WaypointState(pos, waypoint.east(instruction.amount))
                'W' -> WaypointState(pos, waypoint.west(instruction.amount))
                'L' -> WaypointState(
                    pos,
                    (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(acc.y, -acc.x) }
                )
                'R' -> WaypointState(
                    pos,
                    (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(-acc.y, acc.x) }
                )
                else -> WaypointState((1..instruction.amount).fold(pos) { acc, _ -> acc + waypoint }, waypoint)
            }
        }
    }

    // parses input into list of Instructions
    private val instructions = input.lines().map { Instruction(it.first(), it.drop(1).toInt()) }

    // for both parts, start with initial state, execute instructions on each successive state, then take the
    // final location and find the distance from the origin.
    private fun solve(initialState: State): Int {
        return instructions
            .fold(initialState, State::executeInstruction)
            .pos
            .manhattanDistance()
    }

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