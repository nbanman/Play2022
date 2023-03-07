package org.gristle.adventOfCode.y2015.d11

import org.gristle.adventOfCode.Day

class Y2015D11(input: String) : Day {
    private fun isValid(s: String) = hasStraight(s) && noConfusion(s) && twoPairs(s)

    private fun increment(s: String): String {
        val changeIndex = s.indexOfLast { it != 'z' }
        val zs = s.lastIndex - changeIndex
        return s.substring(0, changeIndex) + (s[changeIndex] + 1) + (1..zs).fold("") { acc, _ -> acc + 'a' }
    }

    private fun hasStraight(s: String): Boolean = s
        .windowed(3)
        .any { trip ->
            trip[0].isLowerCase()
                    && trip[1] - trip[0] == 1
                    && trip[2] - trip[1] == 1
        }

    private fun noConfusion(s: String): Boolean = !"""[iol]"""
        .toRegex()
        .containsMatchIn(s)

    private fun twoPairs(s: String): Boolean = """([a-z])\1"""
        .toRegex()
        .findAll(s)
        .count()
        .let { it > 1 }

    private fun nextPassword(password: String) =
        generateSequence(password, ::increment) // sequence starts with password and increments by one
            .drop(1) // do not consider the current password
            .first(::isValid) // get first valid password

    // Answer to part 1 is reused for part 2, so make it lazy so that the calculation counts towards part 1 time.
    private val firstPassword: String by lazy { nextPassword(input) }

    override fun part1() = firstPassword
    override fun part2() = nextPassword(firstPassword)
}

fun main() = Day.runDay(Y2015D11::class)

//    Class creation: 17ms
//    Part 1: hxbxxyzz (25ms)
//    Part 2: hxcaabcc (276ms)
//    Total time: 319ms