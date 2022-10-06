package org.gristle.adventOfCode.y2020.d10

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D10(input: String) {
    // parse adapters from outlet, sort from lowest rating
    private val adapters = input.lines().map { it.toInt() }.sorted()

    // include charging outlet and end device
    private val allDevices = listOf(0) + adapters + (adapters.last() + 3)

    // get list of joltage differences between devices
    private val differences = allDevices
        .zipWithNext() // create previous/next pairs
        .map { (prev, next) -> next - prev } // map delta from prev device

    fun part1() = differences
        .count { it == 3 } // count number of 3-jolt differences
        .let { jolt3s ->
            val jolt1s = differences.size - jolt3s // derive number of 1-jolt differences
            jolt1s * jolt3s
        }

    fun part2() = differences
        .joinToString("") // join differences to one string before splitting in a different way
        .split('3') // both devices 3 apart must be in chain so don't permute
        .map { // each string of 1s represents devices one away from each other.
            // the last '1' in string is required though b/c it's 3 away from the next device
            when (it.length) { // convert to number of permutations for the optional devices
                4 -> 7 // 1111, 1101, 1011, 1001, 0111, 0101, 0011 (0001 not allowed b/c that's 4 apart)
                3 -> 4 // 111, 101, 110, 001  
                2 -> 2 // 11, 01
                else -> 1 // 1
            }
        }.fold(1L) { acc, i -> acc * i } // multiply them by each other

}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D10(readRawInput("y2020/d10"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1890
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 49607173328384
}