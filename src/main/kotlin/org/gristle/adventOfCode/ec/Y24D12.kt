package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.isOdd

fun main() {
    val timer = Stopwatch(true)
    val (input1, input2, input3) = ecInputs(24, 12)
    println("1. ${attackRuins(input1)}: ${timer.lap()}ms")
    println("2. ${attackRuins(input2)}: ${timer.lap()}ms")
    println("3. ${missileCommand(input3)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private enum class Ruins {
    BLOCK,
    ROCK,
}

private fun attackRuins(input: String): Int = getRuins(input).sumOf { target ->
    (0..2).asSequence()
        .mapNotNull { catapult -> getRanking(catapult, target) }
        .first()
}

private fun getRuins(input: String) = input
    .lines()
    .reversed()
    .drop(1)
    .flatMapIndexed { y, line ->
        line.withIndex()
            .mapNotNull { (x, c) ->
                when (c) {
                    'T' -> Coord(x, y) to Ruins.BLOCK
                    'H' -> Coord(x, y) to Ruins.ROCK
                    else -> null
                }
            }
    }.sortedWith(
        compareBy<Pair<Coord, Ruins>> { (pos) -> pos.x }.thenByDescending { (pos) -> pos.y }
    )


private fun getRanking(catapult: Int, target: Pair<Coord, Ruins>): Int? {
    val (pos, ruin) = target
    val diff = pos - Coord(1, catapult)
    val adjX = diff.x + diff.y
    return if (adjX % 3 == 0) {
        val power = adjX / 3
        score(catapult, power, ruin)
    } else {
        null
    }
}

private fun score(catapult: Int, power: Int, ruins: Ruins) =
    (catapult + 1) * power * if (ruins == Ruins.BLOCK) 1 else 2

private fun missileCommand(input: String): Int = input
    .getInts()
    .chunked(2) { (x, y) -> Coord(x, y) }
    .sumOf { meteor ->
        (0..2)
            .mapNotNull { catapult -> intercept(catapult, meteor) }
            .min()
    }

fun intercept(catapult: Int, meteor: Coord): Int? {
    val meteor = if (meteor.x.isOdd()) {
        meteor - Coord(1, 1)
    } else {
        meteor
    }

    val x = meteor.x / 2
    // Step 1: if xm - ym + offset == 0, then you get it on the upswing, and power is xm / 2
    if (meteor.x - meteor.y + catapult == 0) {
        val power = x
        val score = score(catapult, power, Ruins.BLOCK)
        return score
    }

    // Step 2: y == bc == p + offset equation
    // at x, y will have dropped by x amount
    val y = meteor.y - x
    val power = y - catapult
    if (x in power..power * 2) {
        val score = score(catapult, power, Ruins.BLOCK)
        return score
    }

    // Step 3: Apply formula from pts 1 + 2.
    val adjX = x + y - catapult
    return if (adjX % 3 == 0 && y - catapult <= x) {
        val power = adjX / 3
        score(catapult, power, Ruins.BLOCK)
    } else {
        null
    }
}
