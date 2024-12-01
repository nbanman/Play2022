package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Stopwatch

fun main() {
    val timer = Stopwatch(true)
    println("1. ${solve(1, 1354)}: ${timer.lap()}ms")
    println("2. ${solve(2, 5639)}: ${timer.lap()}ms")
    println("3. ${solve(3, 28180)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private fun solve(part: Int): Int = inputs[part - 1]
    .chunked(part)
    .sumOf { baddies ->
        val numberOfBaddies: Int = baddies.count { it != 'x' }
        val potions: (Char) -> Int = { baddie ->
            when (baddie) {
                'A' -> 0
                'B' -> 1
                'C' -> 3
                'D' -> 5
                else -> 0
            }
        }
        val bonusPotions: Int = numberOfBaddies * (numberOfBaddies - 1)
        bonusPotions + baddies.sumOf(potions)
    }

private fun solve(part: Int, expected: Int): String {
    val solution = solve(part)
    val passFail = if (solution == expected) "PASS" else "FAIL ($expected)"
    return "$solution, $passFail"
}

private fun Char.value() = (this.code - 97) * 2 - 1

private val tests = listOf(
    "ABBAC",
    "AxBCDDCAxD",
    "xBxAAABCDxCC",
)

private val inputs = ecInputs(24, 1)
