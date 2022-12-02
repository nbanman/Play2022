package org.gristle.adventOfCode.y2022.d2

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.fmod
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.lines

private typealias Round = Pair<Int, Int>

class Y2022D2(input: String) {

    enum class Throw {
        ROCK, PAPER, SCISSORS;

        val score = ordinal + 1

        companion object {
            fun from(n: Int) = values()[n fmod values().size]
        }
    }

    enum class Outcome {
        LOSE, DRAW, WIN;

        val score = ordinal * 3

        companion object {
            fun from(n: Int) = values()[n fmod values().size]
        }
    }

    private val rounds: List<Round> = input.lines().map { it[0] - 'A' to it[2] - 'X' }

    fun part1() = rounds.sumOf { (opponent, me) ->
        Outcome.from(me + 1 - opponent).score + Throw.from(me).score
    }

    fun part2() = rounds.sumOf { (opponent, me) ->
        Outcome.from(me).score + Throw.from(opponent + me - 1).score
    }
}

fun main() {
    val timer = Stopwatch(start = true)
    val input = getInput(2, 2022)
    val solver = Y2022D2(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 9241
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 14610
    println("Total time: ${timer.elapsed()}ms")
}