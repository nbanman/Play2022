package org.gristle.adventOfCode.y2015.d20

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.foldToList
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

        // println("factors of $number: $factors")
        return factors
    }

    private tailrec fun expandFactors(primeFactors: List<Int>, factors: List<Int> = listOf(1)): List<Int> {
        return if (primeFactors.isNotEmpty()) {
            @Suppress("RemoveExplicitTypeArguments")
            val latest = primeFactors
                .dropLastWhile { it != primeFactors.first() }
                .foldToList<Int, Int> { i ->
                    add(if (isEmpty()) i else last() * i)
                }
            val newFactors = factors.fold(listOf<Int>()) { acc, i ->
                acc + listOf(i) + latest.map { it * i }
            }
            expandFactors(primeFactors - latest.toSet(), newFactors)
        } else {
            factors
        }
    }

    fun part1() = generateSequence(1) { it + 1 }
        .indexOfFirst {
            val elves = expandFactors(primeFactors(it))
            val presents = elves.fold(0) { acc, i -> acc + i * 10 }
            presents >= minimumPresents
        } + 1

    fun part2() = generateSequence(1) { it + 1 }
        .indexOfFirst { houseNumber ->
            val elves = expandFactors(primeFactors(houseNumber)).filter { it * 50 > houseNumber }
            val presents = elves.fold(0) { acc, i -> acc + i * 11 }
            presents >= minimumPresents
        } + 1
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