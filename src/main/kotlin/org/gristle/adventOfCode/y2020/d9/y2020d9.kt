package org.gristle.adventOfCode.y2020.d9

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readInput
import kotlin.math.max

object Y2020D9 {
    private val input = readInput("y2020/d9")

    private val numbers = input.map { it.toLong() }
    fun part1(preamble: Int): Long {
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

    fun part2(weakness: Long): Long {
        var l = 0
        var u = 1
        var sum = numbers[l]
        while (true) {
            sum += numbers[u]
            if (sum == weakness)
                return numbers.slice(l..u).minOrNull()!! + numbers.slice(l..u).maxOrNull()!!
            if (sum > weakness) {
                sum -= numbers[l]
                l++
                if (sum == weakness)
                    return numbers.slice(l..u).minOrNull()!! + numbers.slice(l..u).maxOrNull()!!
                while (sum > weakness) {
                    sum -= numbers[u]
                    u--
                    if (sum == weakness)
                        return numbers.slice(l..u).minOrNull()!! + numbers.slice(l..u).maxOrNull()!!
                }

            }
            u++
        }
    }
}

fun main() {
    var time = System.nanoTime()
    val p1 = Y2020D9.part1(25)
    println("Part 1: $p1 (${elapsedTime(time)}ms)") // 552655238
    time = System.nanoTime()
    println("Part 2: ${Y2020D9.part2(p1)} (${elapsedTime(time)}ms)") // 70672245
}