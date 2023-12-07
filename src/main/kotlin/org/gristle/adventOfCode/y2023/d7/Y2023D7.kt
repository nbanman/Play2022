package org.gristle.adventOfCode.y2023.d7

import org.gristle.adventOfCode.Day

class Y2023D7(input: String) : Day {
    
    private val hands = input
        .lines()
        .map { line -> 
            val (cards, bid) = line.split(' ')
            Hand(cards, bid.toInt())
        }
    
    // takes the hands, sorts by the hand strength as defined by each puzzle part, assigns points using rank and
    // bid amount, then returns sum of all points
    private fun solve(strength: (Hand) -> Int) = hands
        .sortedWith(compareBy(strength))
        .mapIndexed { index, hand -> (index + 1) * hand.bid }
        .sum()
    
    override fun part1() = solve(Hand::strength)

    override fun part2() = solve(Hand::strengthWild)
    
    data class Hand(val cards: String, val bid: Int) {

        // groups cards together, for use in determining handStrength. Sorted because the relative size of the groups
        // is used to determine what kind of hand we have. Then take the two most populous groups in the hand, 
        // then deliver ordered ranking of each hand type. 
        // [biggest group size] * 2 + [2nd biggest group size]. Then normalized to 0..6.
        private val handType: Int = cards
            .groupingBy { it }
            .eachCount()
            .map { (_, amt) -> amt }
            .sortedDescending()
            .let { groups -> (groups[0] * 2 + groups.getOrElse(1) { 0 } - 4).coerceAtLeast(0) }
        
        // strength is an Int that we use to sort with. The most important thing is handType, after that, the value
        // of the cards, in order. E.g., 98 > 8A, because 9 is greater than 8. We can represent this all as an Int
        // concatenating them all, giving 4 bits for each value.
        val strength: Int = cards.fold(handType) { acc, card -> (acc shl 4) + CARD_ORDER_1.indexOf(card) }
        
        // strengthWild does the same thing as strength, but first it adjusts the handType to accommodate Jokers.
        // And it also uses a different card value ranking.
        val strengthWild: Int = let {
            
            // changes the hand type if a joker is found
            val handTypeWild: Int = let {
                val jokers = cards.count { it == 'J' }
                if (jokers > 0) {
                    when (handType) {
                        4, 5, 6 -> 6 // full house to 5-kind all become 5-kind
                        3 -> 5 // 3-kind becomes 4-kind
                        2 -> 3 + jokers // if JJ found, full house, else 3-kind
                        1 -> 3 // pair becomes 3-kind
                        else -> 1 // high card becomes pair
                    }
                } else {
                    handType
                }
            }
                
            cards.fold(handTypeWild) { acc, card -> (acc shl 4) + CARD_ORDER_2.indexOf(card) }
        }
        
        companion object {
            private const val CARD_ORDER_1 = "23456789TJQKA"
            private const val CARD_ORDER_2 = "J23456789TQKA"
        }
    }
}

fun main() = Day.runDay(Y2023D7::class)

//    Class creation: 48ms
//    Part 1: 253866470 (3ms)
//    Part 2: 254494947 (3ms)
//    Total time: 55ms

@Suppress("unused")
private val sampleInput = listOf(
    """32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
""",
)