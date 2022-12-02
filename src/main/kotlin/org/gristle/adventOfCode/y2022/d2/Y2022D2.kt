package org.gristle.adventOfCode.y2022.d2

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.fmod
import org.gristle.adventOfCode.utilities.getInput
import org.gristle.adventOfCode.utilities.lines

class Y2022D2(input: String) {

    // it[0] is the first char in the line representing opponent; convert to int between 0 and 2
    // it[2] is the last char in the line representing me; convert to int between 0 and 2
    private val rounds = input.lines().map { it[0] - 'A' to it[2] - 'X' }

    // Lose = 0 -> 0; Draw = 1 -> 3; Win = 2 -> 6
    private fun outcomeScore(n: Int) = (n fmod 3) * 3

    // Rock = 0 -> 1; Paper = 1 -> 2; Scissors = 2 -> 3 
    private fun throwScore(n: Int) = (n fmod 3) + 1

    fun part1() = rounds.sumOf { (opponentThrow, myThrow) ->
        // the outcomeScore argument subtracts one throw from the other and adds one. Along with the floormod in
        // the function, this finds the outcome of the two throws and scores accordingly.
        // the throwScore argument just takes my throw and scores it accordingly
        outcomeScore(myThrow - opponentThrow + 1) + throwScore(myThrow)
    }

    fun part2() = rounds.sumOf { (opponentThrow, myOutcome) ->
        // The outcomeScore argument just takes my outcome and scores it accordingly.
        // The throwScore argument adds my opponents score to my outcome. Along with the floormod in
        // the function, this finds what my throw is and scores it accordingly.
        outcomeScore(myOutcome) + throwScore(opponentThrow + myOutcome - 1)
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