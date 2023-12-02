package org.gristle.adventOfCode.y2023.d2

import org.gristle.adventOfCode.Day

class Y2023D2(input: String) : Day {

    private val pattern = """(?:\d+ (?:red|green|blue)(?:, )?)+""".toRegex()

    private val games: List<List<Map<String, Int>>> = input
        .lines()
        .map { line ->
            pattern.findAll(line).map { matchResult ->
                buildMap {
                    matchResult
                        .value
                        .split(", ")
                        .forEach { cubeStr ->
                            put(cubeStr.takeLastWhile { it != ' ' }, cubeStr.takeWhile { it.isDigit() }.toInt())
                        }
                }
            }.toList()
        }

    override fun part1(): Int {
        val bag = listOf("red" to 12, "green" to 13, "blue" to 14).toMap()
        return games
            .withIndex()
            .filter { (_, game) ->
                game.all { it.entries.all { (color, amt) -> amt <= bag.getValue(color) } }
            }.sumOf { (index, _) -> index + 1 }
    }

    override fun part2(): Int {
        val answer = games.sumOf { game ->
            val gameScores = listOf("red", "green", "blue").map { color ->
                game.maxOf { bag -> bag[color] ?: 0 }
            }
            gameScores.reduce(Int::times)
        }
        return answer
    }
}

fun main() = Day.runDay(Y2023D2::class)

//    Class creation: 33ms
//    Part 1: 2377 (8ms)
//    Part 2: 71220 (1ms)
//    Total time: 43ms
