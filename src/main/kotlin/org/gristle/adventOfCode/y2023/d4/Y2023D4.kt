package org.gristle.adventOfCode.y2023.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts

class Y2023D4(input: String) : Day {

    private val cards: List<Int> = input
        .lines()
        .map { line ->

            // rather than split the numbers into winners and potentials, rely on the fact that neither side has a 
            // duplicate number. So we can get a list of all the numbers and pull out the ones that are duplicated.
            // the first Int in the line is the card number, which we don't need, so drop that.
            val numbers = line.getInts().drop(1).toList()
            numbers.size - numbers.distinct().size
        }

    // bitwise Int equivalent of 2^(n - 1) returns the point value
    override fun part1() = cards.sumOf { 1 shl it shr 1 }

    override fun part2(): Int {
        // use mutable array to track how many of each card we have
        val cardCount = IntArray(cards.size) { 1 }

        // increase the number of each card we have by going through each card number in turn, checking how many 
        // future cards they create, and updating the cardCount appropriately
        cards.forEachIndexed { index, count ->

            // this is the range of indices in cardCount to be updated. Note that there is no need to check for 
            // overflow beyond the number of cards because the puzzle states, 'Cards will never make you copy a card 
            // past the end of the table.'
            val range = index + 1..index + count

            // there is a multiplier effect for each card of that index you have
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