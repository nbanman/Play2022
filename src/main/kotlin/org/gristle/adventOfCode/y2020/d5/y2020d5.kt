package org.gristle.adventOfCode.y2020.d5

import org.gristle.adventOfCode.utilities.*

object Y2020D5 {
    private val input = readInput("y2020/d5")

    class Ticket(seatNumber: Int) {
        private val row = seatNumber.shr(3)
        private val column = seatNumber.and(7)
        val seatId = row * 8 + column
    }

    private val tickets = input
        .map { line ->
            val seatNumber = line.reversed().foldIndexed(0) { index, acc, c ->
                acc + when(c) {
                    'F' -> 0
                    'L' -> 0
                    else -> 1.shl(index)
                }
            }
            Ticket(seatNumber)
        }

    fun part1() = tickets.maxOf { it.seatId }

    fun part2(): Int {
        val sortedIds = tickets.map { it.seatId }.sorted()
        val offset = sortedIds.first()
        for (i in sortedIds.indices) {
            if (offset + i != sortedIds[i]) return sortedIds[i] - 1
        }
        return -1
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D5.part1()} (${elapsedTime(time)}ms)") // 922
    time = System.nanoTime()
    println("Part 2: ${Y2020D5.part2()} (${elapsedTime(time)}ms)") // 747
}