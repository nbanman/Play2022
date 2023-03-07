package org.gristle.adventOfCode.y2021.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.toGrid

class Y2021D4(input: String) : Day {

    data class BingoCard(val grid: Grid<Int>) {
        private val winConditions = (grid.rows() + grid.columns())
            .map { it.toSet() }

        fun bingo(calledNumbers: List<Int>) = winConditions.any { it.intersect(calledNumbers.toSet()) == it }

        fun score(calledNumbers: List<Int>) = grid.sum() - calledNumbers.intersect(grid).sum()
    }

    private val data = input.split("\n\n")

    private val drawPile = data[0].split(',').map(String::toInt)

    private val bingoCards = data
        .drop(1)
        .map { cardString ->
            cardString
                .split(' ', '\n')
                .mapNotNull { it.toIntOrNull() }
        }.map { BingoCard(it.toGrid(5)) }

    override fun part1(): Int {
        val calledNumbers = drawPile.take(4).toMutableList()
        return drawPile
            .asSequence()
            .drop(4)
            .mapNotNull { number ->
                calledNumbers.add(number)
                bingoCards.find { it.bingo(calledNumbers) }
            }.first()
            .let { bingoCard ->
                bingoCard.score(calledNumbers) * calledNumbers.last()
            }
    }

    override fun part2(): Int {
        val calledNumbers = drawPile.toMutableList()
        return drawPile
            .reversed()
            .asSequence()
            .mapNotNull { lastNumber ->
                calledNumbers.removeLast()
                bingoCards
                    .find { !it.bingo(calledNumbers) }
                    ?.let { lastNumber to it }
            }.first()
            .let { (lastNumber, bingoCard) ->
                bingoCard.score(calledNumbers + lastNumber) * lastNumber
            }
    }
}

fun main() = Day.runDay(Y2021D4::class)

//    Class creation: 30ms
//    Part 1: 39902 (44ms)
//    Part 2: 26936 (6ms)
//    Total time: 81ms