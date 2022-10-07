package org.gristle.adventOfCode.y2020.d12

import org.gristle.adventOfCode.utilities.*

class Y2020D12(input: String) {

    private data class Instruction(val action: Char, val amount: Int)
    private data class State(val pos: Coord, val dir: Nsew, val waypoint: Coord)
    
    private val instructions = input.lines().map { Instruction(it.first(), it.drop(1).toInt()) }

    private fun solve(execute: State.(Instruction) -> State): Int {
        val initialState = State(Coord.ORIGIN, Nsew.EAST, Coord(10, -1))
        return instructions
            .fold(initialState) { state, instruction -> state.execute(instruction) }
            .pos
            .manhattanDistance()
    }

    fun part1() = solve { instruction ->
        when (instruction.action) {
            'N' -> copy(pos = pos.north(instruction.amount))
            'S' -> copy(pos = pos.south(instruction.amount))
            'E' -> copy(pos = pos.east(instruction.amount))
            'W' -> copy(pos = pos.west(instruction.amount))
            'L' -> copy(dir = (1..(instruction.amount / 90)).fold(dir) { acc, _ -> acc.left() })
            'R' -> copy(dir = (1..(instruction.amount / 90)).fold(dir) { acc, _ -> acc.right() })
            else -> copy(pos = pos.move(dir, instruction.amount))
        }
    }

    fun part2() = solve { instruction ->
        when (instruction.action) {
            'N' -> copy(waypoint = waypoint.north(instruction.amount))
            'S' -> copy(waypoint = waypoint.south(instruction.amount))
            'E' -> copy(waypoint = waypoint.east(instruction.amount))
            'W' -> copy(waypoint = waypoint.west(instruction.amount))
            'L' -> copy(waypoint = (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(acc.y, -acc.x) })
            'R' -> copy(waypoint = (1..(instruction.amount / 90)).fold(waypoint) { acc, _ -> Coord(-acc.y, acc.x) })
            else -> copy(pos = (1..instruction.amount).fold(pos) { acc, _ -> acc + waypoint })
        }
    }
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