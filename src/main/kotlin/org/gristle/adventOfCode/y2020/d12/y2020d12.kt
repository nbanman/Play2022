package org.gristle.adventOfCode.y2020.d12

import org.gristle.adventOfCode.utilities.*

object Y2020D12 {
    private val input = readInput("y2020/d12")

    data class Instruction(val action: Char, val amount: Int)
    val instructions = input.map { Instruction(it.first(), it.drop(1).toInt()) }
    fun part1(): Int {
        var dir = Nsew.EAST
        var coord = Coord.ORIGIN
        instructions.forEach { instruction ->
            when (instruction.action) {
                'N' -> coord = coord.north(instruction.amount)
                'S' -> coord = coord.south(instruction.amount)
                'E' -> coord = coord.east(instruction.amount)
                'W' -> coord = coord.west(instruction.amount)
                'L' -> dir = (1..(instruction.amount / 90)).fold(dir) { acc, _ -> acc.left() }
                'R' -> dir = (1..(instruction.amount / 90)).fold(dir) { acc, _ -> acc.right() }
                else -> coord = coord.move(dir, instruction.amount)
            }
        }
        return coord.manhattanDistance()
    }

    fun part2(): Int {
        var coord = Coord(0, 0)
        var wayPoint = coord + Coord(10, -1)
        instructions.forEach { instruction ->
            when (instruction.action) {
                'N' -> wayPoint = wayPoint.north(instruction.amount)
                'S' -> wayPoint = wayPoint.south(instruction.amount)
                'E' -> wayPoint = wayPoint.east(instruction.amount)
                'W' -> wayPoint = wayPoint.west(instruction.amount)
                'L' -> wayPoint = (1..(instruction.amount / 90)).fold(wayPoint) { acc, _ ->
                    Coord(acc.y, -acc.x)
                }
                'R' -> wayPoint = (1..(instruction.amount / 90)).fold(wayPoint) { acc, _ ->
                    Coord(-acc.y, acc.x)
                }
                else -> coord = (1..instruction.amount).fold(coord) { acc, _ ->
                    acc + wayPoint
                }
            }
        }
        return coord.manhattanDistance()
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D12.part1()} (${elapsedTime(time)}ms)") // 2280
    time = System.nanoTime()
    println("Part 2: ${Y2020D12.part2()} (${elapsedTime(time)}ms)") // 38693
}