package org.gristle.adventOfCode.y2020.d9

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.max

class Y2020D9(input: String) {

    private val numbers = input.lines().map { it.toLong() }
    fun part1(): Long {
        val preamble = 25
        // Prep cache
        val cache = mutableMapOf<Long, Int>()
        for (l in 0 until (preamble - 1)) {
            for (u in (l + 1) until preamble) {
                cache[numbers[l] + numbers[u]] = l
            }
        }
        // Try each subsequent number
        for (i in (preamble)..numbers.lastIndex) {
            val current = numbers[i]
            val indexOfSum = cache[current] ?: -1
            if (indexOfSum < i - preamble) {
                return current
            }
            for (l in (i - preamble + 1) until i) {
                cache[numbers[l] + current] = max(cache[numbers[l] + current] ?: -1, l)
            }
        }
        return -1L
    }

    fun part2(): Long {
        val weakness = part1()
        var l = 0
        var u = 1
        var sum = numbers[l]
        while (true) {
            sum += numbers[u]
            if (sum == weakness)
                return numbers.slice(l..u).min() + numbers.slice(l..u).max()
            if (sum > weakness) {
                sum -= numbers[l]
                l++
                if (sum == weakness)
                    return numbers.slice(l..u).min() + numbers.slice(l..u).max()
                while (sum > weakness) {
                    sum -= numbers[u]
                    u--
                    if (sum == weakness)
                        return numbers.slice(l..u).min() + numbers.slice(l..u).max()
                }

            }
            u++
        }
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D9(readRawInput("y2020/d9"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 552655238
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 70672245
}