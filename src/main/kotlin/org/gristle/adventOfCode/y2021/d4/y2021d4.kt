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
        fun bingo(calledNumbers: List<Int>) = winConditions.any { it.intersect(calledNumbers.toSet()) == it }
        
        fun score(calledNumbers: List<Int>) = grid.sum() - calledNumbers.intersect(grid).sum()
    }

    val data = input.split("\n\n")

    private val drawPile = data[0].split(',').map { it.toInt() }

    private val bingoCards = data
        .drop(1)
        .map { cardString ->
            cardString
                .split(' ', '\n')
                .mapNotNull { it.toIntOrNull() }
        }.map { BingoCard(it.toGrid(5)) }
    
    fun part1(): Int {
        val calledNumbers = drawPile.take(4).toMutableList()
        return drawPile.drop(4)
            .asSequence()
            .mapNotNull { number ->
                calledNumbers.add(number)
                bingoCards.find { it.bingo(calledNumbers) }
            }.first()
            .let { bingoCard ->
                bingoCard.score(calledNumbers) * calledNumbers.last()
            }
    } 
        
    fun part2(): Int {
        val calledNumbers = drawPile.toMutableList()
        return drawPile
            .reversed()
            .asSequence()
            .mapNotNull { lastNumber ->
                calledNumbers.remove(lastNumber)
                bingoCards
                    .find { !it.bingo(calledNumbers) }
                    ?.let { lastNumber to it }
            }.first()
            .let { (lastNumber, bingoCard) ->
                bingoCard.score(calledNumbers + lastNumber) * lastNumber
            }
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D4.part1()} (${elapsedTime(time)}ms)") // 39902
    time = System.nanoTime()
    println("Part 2: ${Y2021D4.part2()} (${elapsedTime(time)}ms)") // 26936
}