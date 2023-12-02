package org.gristle.adventOfCode.y2023.d2

import org.gristle.adventOfCode.Day
import kotlin.math.max

class Y2023D2(input: String) : Day {

    // Both parts only care about the maximum of each color cube each bag is shown to hold. Each line is a game,
    // so make a list of games, one per line, represented by a map holding the maximum number of cubes reported for
    // each color.
    private val games: List<Map<String, Int>> = input
        .lines()
        .map { line ->
            buildMap {
                cubePattern
                    .findAll(line)
                    .map(MatchResult::destructured)
                    .forEach { (amt, color) -> this[color] = max(amt.toInt(), this[color] ?: 0) }
            }
        }

    // ID is by order of game, 1 more than the index since the game count starts at 1 and the list count starts at 0. 
    // Filter out the games where the elves' bag does not contain enough cubes to make the game possible. Then add up 
    // the IDs of the remaining games. 
    override fun part1(): Int = games
        .withIndex()
        .filter { (_, game) -> game.all { (color, amt) -> (standardBag[color] ?: 0) >= amt } }
        .sumOf { (index, _) -> index + 1 }

    // Take each game's various maximums and return the sum of their respective products.
    override fun part2(): Int = games.sumOf { it.values.reduce(Int::times) }

    companion object {
        private val cubePattern = Regex("""(\d+) (\w+)""")
        private val standardBag = mapOf("red" to 12, "green" to 13, "blue" to 14)
    }
}

fun main() = Day.runDay(Y2023D2::class)

//    Class creation: 29ms
//    Part 1: 2377 (3ms)
//    Part 2: 71220 (1ms)
//    Total time: 34ms

