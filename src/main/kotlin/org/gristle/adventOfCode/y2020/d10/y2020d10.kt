package org.gristle.adventOfCode.y2020.d10

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readInput

object Y2020D10 {
    private val input = readInput("y2020/d10")
    private val adapters = input.map { it.toInt() }.sorted()
    private val allDevices = listOf(0) + adapters + (adapters.last() + 3)

    fun part1() = allDevices.drop(1).foldIndexed(Triple(0, 0, 0)) { index, acc, i ->
        when {
            i - allDevices[index] == 3 -> Triple(acc.first, acc.second, acc.third + 1)
            i - allDevices[index] == 1 -> Triple(acc.first + 1, acc.second, acc.third)
            else -> Triple(acc.first, acc.second + 1, acc.third)
        }
    }.let { it.first * it.third }

    fun part2(): Long {
        return allDevices
            .drop(1) // start from second device (first adapter)
            .mapIndexed { index, i -> i - allDevices[index] } // map delta from prev device
            .joinToString("") // join them all up before splitting in a different way
            .split('3') // both devices 3 apart must be in chain so don't permute
            .map { // each string of 1s represents devices one away from each other.
                // the last '1' is required though b/c it's 3 away from the next device
                when (it.length) { // convert to number of permutations for the optional devices
                    4 -> 7
                    3 -> 4
                    2 -> 2
                    else -> 1
                }
            }.fold(1L) { acc, i -> acc * i } // multiply them by each other
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D10.part1()} (${elapsedTime(time)}ms)") // 1890 
    time = System.nanoTime()
    println("Part 2: ${Y2020D10.part2()} (${elapsedTime(time)}ms)") // 49607173328384
}