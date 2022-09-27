package org.gristle.adventOfCode.y2020.d5

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readInput

object Y2020D5 {
    private val input = readInput("y2020/d5")

    // The seat id is Row * 8 + Column. Since there are 8 columns this means that the seatIDs are sequential from
    // 0 to 1016, and the FBLR code is just a binary number with 'F' and 'L' meaning '0' and 'B' and 'R' meaning 1.
    private val seatIds = input
        .map { line ->
            line.foldIndexed(0) { index, acc, c ->
                acc + when(c) {
                    'F' -> 0
                    'L' -> 0
                    else -> 1.shl(9 - index)
                }
            }
        }.sorted() // Pt1 needs the largest, and Pt2 needs a full sort, so just go ahead and sort the seatIds

    fun part1() = seatIds.last() // Since sorted, this is the highest seat ID on a boarding pass.

    // The seatIds should all be contiguous. Yours is missing, so look for the first non-contiguous seatId in the
    // sorted list of seatIds. Yours would be the seatId immediately below that.
    fun part2() = seatIds
        .asSequence() // Convert to sequence..
        .zipWithNext() // then convert sequence to include both the previous seatId and the next seatId
        .first { (prev, next) -> prev + 1 != next } // find the first instance where the next seatId is not contiguous
        .second - 1 // return that, minus 1 (since your missing ticket is the contiguous seatId)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D5.part1()} (${elapsedTime(time)}ms)") // 922
    time = System.nanoTime()
    println("Part 2: ${Y2020D5.part2()} (${elapsedTime(time)}ms)") // 747
}