package org.gristle.adventOfCode.y2022.d10

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.ocr
import org.gristle.adventOfCode.utilities.toGrid

class Y2022D10(input: String) {

    private val commands = input.lines()

    enum class State { READY, WORKING }

    class VideoSystem(val commands: List<String>) {
        var index = 0
        var register = 1
        var cycles = 0
        var state = State.READY
        var command = ""

        fun signalStrength() = register * cycles

        val strengths = mutableListOf<Int>()

        val screen = mutableListOf<Boolean>()

        val executeSeq = generateSequence(0) { it + 1 }
            .onEach {
                val pixel = cycles % 40
                val sprite = (register - 1)..(register + 1)
                val lit = pixel in sprite
                screen.add(lit)
                cycles++
                if ((cycles + 20) % 40 == 0) strengths.add(signalStrength())
                when (state) {
                    State.READY -> {
                        command = commands[index]
                        index++
                        when {
                            command.startsWith("addx") -> {
                                state = State.WORKING
                            }
                        }
                    }

                    State.WORKING -> {
                        register += command.takeLastWhile { it != ' ' }.toInt()
                        state = State.READY
                    }
                }
            }

        fun execute() {
            executeSeq.first { index == commands.size }
        }
    }

    fun part1(): Int {
        val vs = VideoSystem(commands)
        vs.execute()
        return vs.strengths.sum()
    }

    fun part2(): String {
        val vs = VideoSystem(commands)
        vs.execute()
        return vs.screen.toGrid(40).ocr() // representation { if (it) '#' else '.' }
    }
}

fun main() {
    val input = listOf(
        getInput(10, 2022),
        """noop
addx 3
addx -5""",
        """addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop"""
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D10(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 
    println("\tPart 2: \n${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}