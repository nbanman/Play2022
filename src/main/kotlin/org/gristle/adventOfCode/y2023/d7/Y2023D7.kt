package org.gristle.adventOfCode.y2023.d7

import org.gristle.adventOfCode.Day

class Y2023D7(input: String) : Day {

    private val hands = input
        .lines()
        .map { line -> 
            val (cards, bid) = line.split(' ')
            Hand(cards, bid.toInt())
        }
    
    private fun solve(strength: (Hand) -> Int) = hands
        .sortedWith(compareBy(strength))
        .mapIndexed { index, hand -> (index + 1) * hand.bid }
        .sum()

    override fun part1() = solve(Hand::strength)

    override fun part2() = solve(Hand::strengthWild)
    
    data class Hand(val cards: String, val bid: Int) {
        private val handCount: List<Pair<Char, Int>> = cards
            .groupingBy { it }
            .eachCount()
            .map { (card, amt) -> card to amt }
            .sortedWith(compareByDescending { (_, amt) -> amt })

        private val handStrength: Int = handCount
            .take(2)
            .foldIndexed(0) { index, acc, (_, amt) -> acc + (2 - index) * amt }
            .let { raw -> (raw - 4).coerceAtLeast(0) }
        
        val strength: Int = cards.fold(handStrength) { acc, card -> (acc shl 4) + cardValues.getValue(card) }
        val strengthWild: Int = let {
            val handStrengthWild: Int =
                if (cards.contains('J')) {
                    when (handStrength) {
                        4, 5, 6 -> 6
                        3 -> 5
                        2 -> if (handCount.last().first == 'J') 4 else 5
                        1 -> 3
                        else -> 1
                    }
                } else {
                    handStrength
                }
            cards.fold(handStrengthWild) { acc, card ->
                (acc shl 4) + cardValuesWild.getValue(card)
            }
        }
        
        companion object {
            private const val CARD_ORDER_1 = "23456789TJQKA"
            private const val CARD_ORDER_2 = "J23456789TQKA"

            private val cardValues: Map<Char, Int> = CARD_ORDER_1
                .mapIndexed { index, c -> c to index }
                .toMap()

            private val cardValuesWild: Map<Char, Int> = CARD_ORDER_2
                .mapIndexed { index, c -> c to index }
                .toMap()
        }
    }
}

fun main() = Day.runDay(Y2023D7::class)

@Suppress("unused")
private val sampleInput = listOf(
    """32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
""",
)