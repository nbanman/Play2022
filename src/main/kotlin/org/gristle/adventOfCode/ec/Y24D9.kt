package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getIntList
import kotlin.math.ceil

fun main() {
    val (input1, input2, input3) = ecInputs(24, 9)
    val timer = Stopwatch(true)
    println("1. ${part1(input1)}: ${timer.lap()}ms")
    println("2. ${part2(input2)}: ${timer.lap()}ms")
    println("3. ${part3(input3)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private fun part1(input: String): Int {
    val stamps = listOf(1, 3, 5, 10).reversed()
    val brightnesses: List<Int> = input.getIntList()
    val brightest: Int = brightnesses.max()
    val cache: MutableList<Int?> = MutableList(brightest + 1) { null }
    return solve(brightnesses, stamps, cache)
}

private fun part2(input: String): Int {
    val stamps = listOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30).reversed()
    val brightnesses: List<Int> = input.getIntList()
    val brightest: Int = brightnesses.max()
    val cache: MutableList<Int?> = MutableList(brightest + 1) { null }
    return solve(brightnesses, stamps, cache)
}

private fun part3(input: String): Int {
    val stamps = listOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30, 37, 38, 49, 50, 74, 75, 100, 101).reversed()
    val brightnesses: List<Int> = input.getIntList()
    val brightest: Int = brightnesses.max()
    val cache: MutableList<Int?> = MutableList(brightest + 1) { null }
    for (stamp in stamps) {
        cache[stamp] = 1
    }
    return brightnesses
        .map { brightness ->
            val half = brightness / 2
            val limit = if (brightness and 1 == 1) 49 else 50
            (0..limit)
                .minOf { n ->
                    val ba = half - n
                    val bb = (brightness - half) + n
                    val ba_beetles = cache[ba] ?: getBeetles(ba, Int.MAX_VALUE, cache, stamps)
                    val bb_beetles = cache[bb] ?: getBeetles(bb, Int.MAX_VALUE, cache, stamps)
                    ba_beetles + bb_beetles
                }
        }.sum()
}

fun getBeetles(
    remaining: Int,
    prevBest: Int,
    cache: MutableList<Int?>,
    stamps: List<Int>
): Int = cache[remaining] ?: run {
    var best = prevBest
    outer@for ((idx, stamp) in stamps.withIndex()) {
        val beetles = remaining / stamp
        if (beetles >= best) return@getBeetles best
        if (remaining % stamp == 0) {
            if (cache[remaining] == null) {
                cache[remaining] = beetles
            }
            return@getBeetles beetles
        }

        for (n in beetles downTo 1) {
            val nextStamp = stamps.getOrNull(idx + 1)
            if (nextStamp != null) {
                val nextStampBeetles = ceil((remaining - n * stamp).toFloat() / nextStamp.toFloat()).toInt()
                if (n + nextStampBeetles >= best) break@outer
            }
            if (best <= n) { continue }
            val result = n + getBeetles(
                remaining - n * stamp,
                best - n,
                cache,
                stamps.drop(idx)
            )
            if (result < best) {
                best = result
            }
        }
    }
    cache[remaining] = best
    best
}


private fun solve(brightnesses: List<Int>, stamps: List<Int>, cache: MutableList<Int?>): Int {
    for (stamp in stamps) {
        cache[stamp] = 1
    }
    return brightnesses
        .sumOf { brightness ->
            cache[brightness] ?: getBeetles(brightness, Int.MAX_VALUE, cache, stamps)
        }
}

