package org.gristle.adventOfCode.y2016.d1

import org.gristle.adventOfCode.utilities.*

object Y2016D1 {
    private val input = readRawInput("y2016/d1")

    data class Instruction(val turn: Char, val distance: Int)

    data class Me(val dir: Nsew = Nsew.NORTH, val coord: Coord = Coord.ORIGIN)

    val instructions = input
        .split(", ")
        .map { Instruction(it[0], it.drop(1).toInt()) }

    fun part1(): Int {
        val hq = instructions.fold(Me()) { acc, instruction ->
            newMe(instruction, acc)
        }
        return hq.coord.manhattanDistance()
    }

    private fun newMe(instruction: Instruction, me: Me) = when (instruction.turn) {
        'L' -> Me(me.dir.left(), me.coord.move(me.dir.left(), instruction.distance))
        else -> Me(me.dir.right(), me.coord.move(me.dir.right(), instruction.distance))
    }

    tailrec fun part2(
        me: Me = Me(),
        visited: Set<Coord> = setOf(Coord.ORIGIN),
        instructions: List<Instruction> = this.instructions
    ): Int {
        val instruction = instructions.first()
        val newDir = if (instruction.turn == 'L') me.dir.left() else me.dir.right()
        val newVisited = mutableSetOf<Coord>()
        for (i in 1..instruction.distance) {
            val newPos = me.coord.move(newDir, i)
            if (visited.contains(newPos)) return newPos.manhattanDistance() else newVisited.add(newPos)
        }
        return part2(
            newMe(instruction, me),
            visited + newVisited,
            instructions.drop(1)
        )
    }}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D1.part1()} (${elapsedTime(time)}ms)") // 226
    time = System.nanoTime()
    println("Part 2: ${Y2016D1.part2()} (${elapsedTime(time)}ms)") // 79
}