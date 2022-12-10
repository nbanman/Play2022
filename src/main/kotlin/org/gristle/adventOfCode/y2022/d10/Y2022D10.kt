package org.gristle.adventOfCode.y2022.d10

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.ocr
import org.gristle.adventOfCode.utilities.toGrid

class Y2022D10(input: String) {

    private val commands = input.lines()

    enum class State { READY, WORKING }

    fun solve(commands: List<String>, part2: Boolean = false): String {
        var strengths = 0
        val screen = mutableListOf<Boolean>()

        var index = 0
        var register = 1
        var state = State.READY
        var command = ""

        generateSequence(1) { it + 1 }
            .onEach { cycles ->
                val pixel = (cycles - 1) % 40
                val sprite = (register - 1)..(register + 1)
                screen.add(pixel in sprite)
                if ((cycles + 20) % 40 == 0) strengths += register * cycles
                when (state) {
                    State.READY -> {
                        command = commands[index]
                        index++
                        if (command.startsWith("addx")) state = State.WORKING
                    }

                    State.WORKING -> {
                        register += command.takeLastWhile { it != ' ' }.toInt()
                        state = State.READY
                    }
                }
            }.first { index == commands.size }

        return if (part2) screen.toGrid(40).ocr() else strengths.toString()
    }

    fun part1() = solve(commands)
    fun part2() = solve(commands, part2 = true)
}

fun main() {
    val input = getInput(10, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D10(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}