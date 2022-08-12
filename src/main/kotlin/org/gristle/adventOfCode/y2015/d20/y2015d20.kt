package org.gristle.adventOfCode.y2015.d20

import org.gristle.adventOfCode.utilities.*
import kotlin.math.sqrt

object Y2015D20 {
    private const val input = 33_100_000

    fun primeFactors(number: Int): List<Int> {
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

        // println("factors of $number: $factors")
        return factors
    }

    tailrec fun expandFactors(primeFactors: List<Int>, factors: List<Int> = listOf(1)): List<Int> {
        return if (primeFactors.isNotEmpty()) {
            val latest = primeFactors
                .dropLastWhile { it != primeFactors.first() }
                .foldToList<Int, Int> { i ->
                    add(if (isEmpty()) i else last() * i)
                }
            val newFactors = factors.fold(listOf<Int>()) { acc, i ->
                acc + listOf(i) + latest.map { it * i }
            }
            expandFactors(primeFactors - latest, newFactors)
        } else {
            factors
        }
    }

    fun part1() = generateSequence(1) { it + 1 }
        .indexOfFirst {
            val elves = expandFactors(primeFactors(it))
            val presents = elves.fold(0) { acc, i -> acc + i * 10 }
            presents >= input
        } + 1

    fun part2() = generateSequence(1) { it + 1 }
        .indexOfFirst { houseNumber ->
            val elves = expandFactors(primeFactors(houseNumber)).filter { it * 50 > houseNumber }
            val presents = elves.fold(0) { acc, i -> acc + i * 11 }
            presents >= input
        } + 1
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D20.part1()} (${elapsedTime(time)}ms)") // 776160
    time = System.nanoTime()
    println("Part 2: ${Y2015D20.part2()} (${elapsedTime(time)}ms)") // 786240
}