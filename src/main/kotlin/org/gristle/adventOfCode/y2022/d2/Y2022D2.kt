package org.gristle.adventOfCode.y2022.d2

import org.gristle.adventOfCode.Day

class Y2022D2(input: String) : Day {
    // This solution depends on modular arithmetic and establishing a mathematical relationship between two throws
    // and one outcome. 

    // Throws can be represented as Integers, starting at Rock (0), losing to Paper (1), losing to Scissors (2).
    // We can make the relationship circular using floor mod. 012012012 etc. If circular, then the digit above
    // always represents a win, and the digit below always represents a loss. 

    // Outcomes can also be represented as Integers, starting at Loss(0), then, Draw(1), then Win(2).

    // These assigned Integers are also coincidentally very good for the scoring system, as throw scores are simply
    // ordinal value + 1, and outcome scores are ordinal value * 3. Like so:
    private fun throwScore(myThrow: Int) = myThrow + 1
    private fun outcomeScore(myOutcome: Int) = myOutcome * 3

    // Now to establish the relationship between throws and scores. The equation to solve for outcome is:
    //     myOutcome = (myThrow - opponentThrow + 1).mod(3)
    // We subtract the two throws to get the relationship between the two. A draw would mean 0 difference between two
    // throws. We add an offset of 1 to get it to correspond with the ordinals assigned to the outcomes.
    // 0 + 1 = 1 (draw). A win is 1 above, which is also intuitive. 1 + 1 = 2 (win). A loss is two above, which 
    // is where the mod comes in. Two above is the same as one below, corresponding to a loss. 
    // 2 + 1 = 3 mod 3 = 0 (loss). Thus:
    private fun myOutcome(myThrow: Int, opponentThrow: Int) = (myThrow - opponentThrow + 1).mod(3)

    // Part 2 says that my ordinal represents my outcome rather than my throw. So we need to solve for
    // my throw. We rearrange the above equation with simple algebra, again wrapping it in a floor mod function to keep
    // the values between 0 and 2:
    //     myThrow = (myOutcome + opponentThrow - 1).mod(3)
    private fun myThrow(myOutcome: Int, opponentThrow: Int) = (myOutcome + opponentThrow - 1).mod(3)

    // Parsing input:
    // it[0] is the first char in the line representing opponent's throw; convert to int between 0 and 2
    // it[2] is the last char in the line representing me (throw for part1, outcome for part2; convert to int 
    // between 0 and 2
    private val rounds = input.lines().map { it[0] - 'A' to it[2] - 'X' }

    // Part 1 tells us to interpret my number as my throw. So the task is to derive the outcome, then score accordingly.
    override fun part1() = rounds.sumOf { (opponentThrow, myThrow) ->
        val myOutcome = myOutcome(myThrow, opponentThrow)
        outcomeScore(myOutcome) + throwScore(myThrow)
    }

    // Part 2 tells us to interpret my number as my outcome. So the task is to derive my throw, then score accordingly.
    override fun part2() = rounds.sumOf { (opponentThrow, myOutcome) ->
        val myThrow = myThrow(myOutcome, opponentThrow)
        outcomeScore(myOutcome) + throwScore(myThrow)
    }
}

fun main() = Day.runDay(Y2022D2::class)

//    Class creation: 23ms
//    Part 1: 9241 (0ms)
//    Part 2: 14610 (0ms)
//    Total time: 24ms