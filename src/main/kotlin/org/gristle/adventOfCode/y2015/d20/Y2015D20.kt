package org.gristle.adventOfCode.y2015.d20

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.sqrt

class Y2015D20(input: String) {

    private val minimumPresents = input.toInt()
    private fun primeFactors(number: Int): List<Int> {
        val factors = mutableListOf<Int>()
        var n = number

        while (n % 2 == 0) {
            factors.add(2)
            n /= 2
        }

        for (i in 3..sqrt(n.toFloat()).toInt() step 2) {
            while (n % i == 0) {
                factors.add(i)
                n /= i
            }
        }

        if (n > 2) factors.add(n)

        return factors
    }

    private tailrec fun expandFactors(primeFactors: List<Int>, factors: List<Int> = listOf(1)): List<Int> {
        return if (primeFactors.isNotEmpty()) {
            val latest = primeFactors
                .dropLastWhile { it != primeFactors.first() }
                .drop(1)
                .runningFold(primeFactors.first(), Int::times)
            val newFactors = factors.fold(listOf<Int>()) { acc, i ->
                acc + listOf(i) + latest.map { it * i }
            }
            expandFactors(primeFactors - latest.toSet(), newFactors)
        } else {
            factors
        }
    }

    fun solve(multiplier: Int, filter: (houseNumber: Int, elf: Int) -> Boolean) = generateSequence(1) { it + 1 }
        .indexOfFirst { houseNumber ->
            val elves = expandFactors(primeFactors(houseNumber)).filter { filter(houseNumber, it) }
            val presents = elves.fold(0) { acc, i -> acc + i * multiplier }
            presents >= minimumPresents
        } + 1


    fun part1() = solve(10) { _, _ -> true }

    fun part2() = solve(11) { houseNumber, elf -> elf * 50 > houseNumber }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D20(readRawInput("y2015/d20"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 776160
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 786240
}