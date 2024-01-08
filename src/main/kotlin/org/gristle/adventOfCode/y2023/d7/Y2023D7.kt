package org.gristle.adventOfCode.y2023.d7

import org.gristle.adventOfCode.Day

class Y2023D7(private val input: String) : Day {
    
    // takes the hands, sorts by the hand strength as defined by each puzzle part, assigns points using rank and
    // bid amount, then returns sum of all points
    private inline fun solve(crossinline swapJacksWithJokers: (String) -> String = { it }) = input
        .lines()
        .map { line ->
            val (cards, bid) = line.split(' ')
            Hand(swapJacksWithJokers(cards), bid.toInt())
        }.sorted()
        .mapIndexed { index, hand -> (index + 1) * hand.bid }
        .sum()
    
    override fun part1() = solve()

    // Part 2 is the same thing, except 'J' cards are replaced by 'ĵ' cards, which are recognized as jokers and
    // are dealt with appropriately in the Hand class logic.
    override fun part2() = solve { it.replace('J', 'ĵ') }
    
    data class Hand(val cards: String, val bid: Int): Comparable<Hand> {

        // strength is an Int that we use to sort with. The most important component of strength  is the strength 
        // of the type of hand. After that, the value of the cards, in order. E.g., 98 > 8A, because 9 is greater 
        // than 8. We can represent this all as an Int concatenating them all, giving 4 bits for each value.
        private val strength: Int = let {
            // number of jokers gets added to the most numerous of the other cards to make the most powerful hand
            val jokers = cards.count { it == 'ĵ' }

            // groups cards together, for use in determining handTypeStrength. Sorted because the relative size 
            // of the groups is used to determine what kind of hand we have. Then take the two most populous groups 
            // in the hand, then deliver ordered ranking of each hand type.
            val groups: List<Int> = cards.filter { it != 'ĵ' }
                .groupingBy { it }
                .eachCount()        // count the number of each card we have
                .values             // we only care about biggest group size, not contents of group
                .sortedDescending() // we only want the two largest groups

            // this gives a strength, from weakest to strongest, of 3, 5, 6, 7, 8, 9, 10
            // we use getOrElse for the second group because some combinations have no second group
            val handTypeStrength = (groups.getOrElse(0) { 0 } + jokers) * 2 + groups.getOrElse(1) { 0 }

            // calculate overall strength by building an int with bitshifts
            cards.fold(handTypeStrength) { acc, card -> (acc shl 4) + CARD_ORDER.indexOf(card) }
        }

        override fun compareTo(other: Hand): Int = this.strength - other.strength

        companion object {
            private const val CARD_ORDER = "ĵ23456789TJQKA"
        }
    }
}

fun main() = Day.runDay(Y2023D7::class)

//    Class creation: 2ms
//    Part 1: 253866470 (30ms)
//    Part 2: 254494947 (7ms)
//    Total time: 40ms

@Suppress("unused")
private val sampleInput = listOf(
    """32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
""",
)