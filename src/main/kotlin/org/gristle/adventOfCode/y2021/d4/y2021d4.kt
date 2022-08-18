package org.gristle.adventOfCode.y2021.d4

import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readStrippedInput
import org.gristle.adventOfCode.utilities.toGrid

object Y2021D4 {
    private val input = readStrippedInput("y2021/d4")

    data class BingoCard(val grid: Grid<Int>) {
        private val winConditions = (grid.rows() + grid.columns())
            .map { it.toSet() }
        fun bingo(calledCards: Set<Int>): Int {
            val winner = winConditions.find { it.intersect(calledCards) == it }
            return if (winner == null) {
                -1
            } else {
                grid.sum() - calledCards.intersect(grid).sum()
            }
        }
    }

    val data = input.split("\n\n")

    private val drawPile = data[0].split(',').mapNotNull { it.toIntOrNull() }

    private val bingoCards = data
        .drop(1)
        .map { cardString ->
            cardString
                .split(' ', '\n')
                .mapNotNull { it.toIntOrNull() }
        }.map { BingoCard(it.toGrid(5)) }

    fun part1(): Int {
        val nextCard = drawPile.iterator()
        val calledCards = mutableSetOf<Int>()
        while (nextCard.hasNext()) {
            val lastCard = nextCard.next()
            calledCards.add(lastCard)
            val winner = bingoCards.find { it.bingo(calledCards) > 0 }
            if (winner != null) {
                return lastCard * winner.bingo(calledCards)
            }
        }
        return -1
    }

    fun part2(): Int {
        val nextCard = drawPile.iterator()
        val bingoSet = bingoCards.toMutableSet()
        val calledCards = mutableSetOf<Int>()
        var latestScore = 0
        while (nextCard.hasNext()) {
            val lastCard = nextCard.next()
            calledCards.add(lastCard)
            val winner = bingoSet.filter { it.bingo(calledCards) > 0 }
            if (winner.isNotEmpty()) {
                bingoSet.removeAll(winner.toSet())
                latestScore = lastCard * winner.first().bingo(calledCards)
            }
        }
        return latestScore
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D4.part1()} (${elapsedTime(time)}ms)") // 39902
    time = System.nanoTime()
    println("Part 2: ${Y2021D4.part2()} (${elapsedTime(time)}ms)") // 26936
}