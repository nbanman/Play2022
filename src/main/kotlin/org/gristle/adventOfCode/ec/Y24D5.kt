package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.pow
import java.util.LinkedList

fun main() {
    val (input1, input2, input3) = ecInputs(24, 5)
    val timer = Stopwatch(true)
    println("1. ${part1(input1)}: ${timer.lap()}ms")
    println("2. ${part2(input2)}: ${timer.lap()}ms")
    println("3. ${part3(input3)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private fun parseInput(input: String): Pair<Int, List<LinkedList<Int>>> {
    val numberOfColumns = 1 + input
        .takeWhile { it != '\n' }
        .count { it == ' ' }
    val columns = List(numberOfColumns) { LinkedList<Int>() }
    input.getInts().forEachIndexed { idx, n ->
        columns[idx % numberOfColumns].addLast(n)
    }
    return numberOfColumns to columns
}

private fun playRound(round: Int, numberOfColumns: Int, columns: List<LinkedList<Int>>): String {
    val clapperCol = (round - 1) % numberOfColumns
    val nextCol = round % numberOfColumns
    val nextLen = columns[nextCol].size
    val clapper = columns[clapperCol].removeFirst()
    val pos = ((clapper - 1) % (nextLen * 2))
        .let { it.coerceAtMost(nextLen) - (it - nextLen).coerceAtLeast(0) }
    columns[nextCol].add(pos, clapper)
    return columns
        .map { column -> column.peekFirst() }
        .joinToString("")
}

private fun part1(input: String): Int {
    val (numberOfColumns, columns) = parseInput(input)
    return generateSequence(1) { round -> round + 1 }
        .map { round -> playRound(round, numberOfColumns, columns) }
        .take(10)
        .last()
        .toInt()
}

private fun part2(input: String): Long {
    val (numberOfColumns, columns) = parseInput(input)
    val digits = input.lineSequence().first().count { it.isDigit() }
    val counter = IntArray(10.pow(digits).toInt())
    return generateSequence(1) { round -> round + 1 }
        .map { round ->
            val shouted = playRound(round, numberOfColumns, columns).toInt()
            round to shouted
        }.first { (round, shouted) ->
            counter[shouted] += 1
            counter[shouted] == 2024
        }.let { (round, shouted) -> round.toLong() * shouted }
}

private fun part3(input: String): Long {
    val (numberOfColumns, columns) = parseInput(input)
    val cache = mutableSetOf<String>()
    var highestNumber = 0L
    var round = 1
    do {
        val shouted = playRound(round, numberOfColumns, columns).toLong()
        if (highestNumber < shouted) highestNumber = shouted
        val state = columns
            .flatMap { column -> column.map { n -> n.toString() } }
            .joinToString("")
        round++
    } while (cache.add(state))
    return highestNumber
}