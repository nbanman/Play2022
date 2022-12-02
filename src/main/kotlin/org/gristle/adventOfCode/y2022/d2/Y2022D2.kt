package org.gristle.adventOfCode.y2022.d2

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.lines

class Y2022D2(input: String) {

    enum class Hand {
        Rock {
            override val beats: Hand
                get() = Scissors
            override val loses: Hand
                get() = Paper
            override val ties: Hand
                get() = Rock
        },
        Paper {
            override val beats: Hand
                get() = Rock
            override val loses: Hand
                get() = Scissors
            override val ties: Hand
                get() = Paper
        },
        Scissors {
            override val beats: Hand
                get() = Paper
            override val loses: Hand
                get() = Rock
            override val ties: Hand
                get() = Scissors
        };

        abstract val beats: Hand
        abstract val loses: Hand
        abstract val ties: Hand
    }

    val code = mapOf(
        'A' to Hand.Rock,
        'B' to Hand.Paper,
        'C' to Hand.Scissors,
        'X' to Hand.Rock,
        'Y' to Hand.Paper,
        'Z' to Hand.Scissors
    )

    data class Round(val opponent: Hand, val xyz: Char, val code: Map<Char, Hand>) {

        private fun shapeScore(shape: Hand) = when (shape) {
            Hand.Rock -> 1
            Hand.Paper -> 2
            Hand.Scissors -> 3
        }

        private val outcomeScore = when (opponent) {
            Hand.Rock -> {
                when (xyz) {
                    'X' -> 3
                    'Y' -> 6
                    else -> 0
                }
            }

            Hand.Paper -> {
                when (xyz) {
                    'X' -> 0
                    'Y' -> 3
                    else -> 6
                }
            }

            Hand.Scissors -> {
                when (xyz) {
                    'X' -> 6
                    'Y' -> 0
                    else -> 3
                }
            }
        }

        private val newOutcomeScore = when (xyz) {
            'X' -> 0
            'Y' -> 3
            else -> 6
        }

        private val newShapeScore = let {
            val shape = when (xyz) {
                'X' -> opponent.beats
                'Y' -> opponent.ties
                else -> opponent.loses
            }
            when (shape) {
                Hand.Rock -> 1
                Hand.Paper -> 2
                Hand.Scissors -> 3
            }
        }

        val score = shapeScore(code.getValue(xyz)) + outcomeScore

        val newScore = newOutcomeScore + newShapeScore
    }

    private val lines = input.lines().map { line ->
        Round(code.getValue(line[0]), line[2], code)
    }

    fun part1() = lines.sumOf(Round::score)

    fun part2() = lines.sumOf(Round::newScore)
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