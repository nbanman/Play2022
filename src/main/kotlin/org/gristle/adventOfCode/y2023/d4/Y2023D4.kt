package org.gristle.adventOfCode.y2023.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList
import kotlin.math.pow

class Y2023D4(input: String) : Day {

    private val cards: List<Int>

    init {
        val leftOffset = input.indexOf(':')

        cards = input
            .lines()
            .map { line ->
                val ints = line.substring(leftOffset).getIntList()
                ints.size - ints.distinct().size
            }
    }

    override fun part1() = cards.sumOf { 2.0.pow(it - 1).toInt() }

    override fun part2(): Int {
        val cardCount = IntArray(cards.size) { 1 }
        cards.forEachIndexed { index, count ->
            val range = index + 1..index + count
            val numberOfCards = cardCount[index]
            range.forEach { cardCount[it] += numberOfCards }
        }
        return cardCount.sum()
    }
}

fun main() = Day.runDay(Y2023D4::class)

//    Class creation: 24ms
//    Part 1: 23750 (0ms)
//    Part 2: 13261850 (0ms)
//    Total time: 24ms

@Suppress("unused")
private val sampleInput = listOf(
    """Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11""",
)