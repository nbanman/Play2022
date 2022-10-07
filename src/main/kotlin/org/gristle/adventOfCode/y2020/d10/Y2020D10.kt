package org.gristle.adventOfCode.y2020.d10

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D10(input: String) {
    // parse adapters from outlet, sort from lowest rating. Then add charging outlet and end devices.
    // Finally convert to a list of the joltage differences between devices.
    private val joltageDifferences = input // raw String
        .lines() // to List of String broken up by line
        .map { it.toInt() } // convert List<String> to List<Int> 
        .sorted() // sort List lowest to highest
        .let { adapters -> listOf(0) + adapters + (adapters.last() + 3) } // add charging outlet and end device to List
        .zipWithNext() // create List of previous/next pairs
        .map { (prev, next) -> next - prev } // map to List of delta from prev device

    fun part1() = joltageDifferences
        .count { it == 3 } // count number of 3-jolt differences
        .let { jolt3s ->
            val jolt1s = joltageDifferences.size - jolt3s // derive number of 1-jolt differences
            jolt1s * jolt3s // answer to pt 1
        }

    // basic idea to reduce the calculations is divide and conquer. Wherever there is a 3-jolt difference
    // that adapter and the adapter before it *must* be in the combination. So split the list using the 3-jolt 
    // differences. You then have a bunch of sublists with 1-jolt differences. The maximum number of 1s you see is
    // 4, so you can use a lookup table to count the number of possible permutations in each sublist. Multiply them all 
    // together and you get your answer.
    fun part2() = joltageDifferences
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
        }.fold(1L) { acc, i -> acc * i } // multiply them by each other to get answer to pt 2
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