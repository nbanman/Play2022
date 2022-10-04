package org.gristle.adventOfCode.y2021.d2

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2021D2(input: String) {

    sealed class Command(val amt: Int) {
        abstract fun execute1(coord: Coord): Coord
        abstract fun execute2(heading: Pair<Coord, Int>): Pair<Coord, Int>
        companion object {
            fun fromGv(gv: List<String>): Command {
                val amt = gv[1].toInt()
                return when (gv[0]) {
                    "forward" -> Forward(amt)
                    "up" -> Up(amt)
                    else -> Down(amt)
                }
            }
        }

        class Forward(amt: Int) : Command(amt) {
            override fun execute1(coord: Coord) = coord.east(amt)

            override fun execute2(heading: Pair<Coord, Int>) =
                heading.first.east(amt).south(heading.second * amt) to heading.second
        }

        class Up(amt: Int) : Command(amt) {
            override fun execute1(coord: Coord) = coord.north(amt)

            override fun execute2(heading: Pair<Coord, Int>) = heading.first to heading.second - amt
        }

        class Down(amt: Int) : Command(amt) {
            override fun execute1(coord: Coord) = coord.south(amt)

            override fun execute2(heading: Pair<Coord, Int>) = heading.first to heading.second + amt
        }
    }

    val pattern = """(forward|down|up) (\d+)""".toRegex()

    val commands = input
        .groupValues(pattern)
        .map { Command.fromGv(it) }

    fun part1() = commands
        .fold(Coord(0, 0)) { acc, command -> command.execute1(acc) }
        .let { it.x * it.y }

    fun part2() = commands
        .fold(Coord(0, 0) to 0) { acc, command -> command.execute2(acc) }
        .let { it.first.x * it.first.y }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D2(readRawInput("y2021/d2"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 2117664
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2073416724
}