package org.gristle.adventOfCode.y2022.d2

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.fmod
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.lines

class Y2022D2(input: String) {

    enum class Throw {
        ROCK, PAPER, SCISSORS;

        val score: Int get() = ordinal + 1
    }

    enum class Outcome {
        LOSE, DRAW, WIN;

        val score: Int get() = ordinal * 3
    }

    private val codeThrow = mapOf(
        'A' to Throw.ROCK,
        'B' to Throw.PAPER,
        'C' to Throw.SCISSORS,
        'X' to Throw.ROCK,
        'Y' to Throw.PAPER,
        'Z' to Throw.SCISSORS,
    )

    private val codeOutcome = mapOf(
        'X' to Outcome.LOSE,
        'Y' to Outcome.DRAW,
        'Z' to Outcome.WIN,
    )

    data class Round(val opponentThrow: Throw, val myThrow: Throw, val myOutcome: Outcome) {
        fun part1Score(): Int {
            val newOutcome = Outcome.values()[(myThrow.ordinal + 1 - opponentThrow.ordinal) fmod 3]
            return newOutcome.score + myThrow.score
        }

        fun part2Score(): Int {
            val newThrow = Throw.values()[(opponentThrow.ordinal + myOutcome.ordinal - 1) fmod 3]
            return newThrow.score + myOutcome.score
        }
    }

    private val rounds = input.lines().map {
        Round(
            codeThrow.getValue(it[0]),
            codeThrow.getValue(it[2]),
            codeOutcome.getValue(it[2])
        )
    }

    fun part1() = rounds.sumOf(Round::part1Score)

    fun part2() = rounds.sumOf(Round::part2Score)
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