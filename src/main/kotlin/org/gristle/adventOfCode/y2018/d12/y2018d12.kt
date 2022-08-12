package org.gristle.adventOfCode.y2018.d12

import org.gristle.adventOfCode.utilities.*

object Y2018D12 {
    private val inp = readInput("y2018/d12")

    fun solve(generations: Int, gen2: Long = 0): Long {
        var plants = inp[0].drop(15)

        val commands = inp
            .drop(2)
            .filter { it.last() == '#'}
            .map { it.take(5) to it.last() }
            .toTypedArray()
            .let { mapOf(*it) }

        for (gen in 1..generations) {
            plants = "..$plants.."
            plants = plants
                .mapIndexed { index, _ ->
                    val pattern = buildString {
                        append(if (index - 2 < 0) '.' else plants[index - 2])
                        append(if (index - 1 < 0) '.' else plants[index - 1])
                        append(plants[index])
                        append(if (index + 1 > plants.lastIndex) '.' else plants[index + 1])
                        append(if (index + 2 > plants.lastIndex) '.' else plants[index + 2])
                    }
                    if (commands[pattern] != null) {
                        '#'
                    } else {
                        '.'
                    }
                }.joinToString("")
        }
        return plants
            .mapIndexed { index, c -> if (c == '#') (index - (generations * 2)) else 0 }
            .sum()
            .toLong()
            .let { if (gen2 != 0L) it + (generations - 100) * (gen2 - generations) else it }

    }
    
    fun part1() = solve(20)

    fun part2() = solve(153, 50_000_000_000)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2018D12.part1()} (${elapsedTime(time)}ms)") // 4110
    time = System.nanoTime()
    println("Part 2: ${Y2018D12.part2()} (${elapsedTime(time)}ms)") // 2650000000466 
}