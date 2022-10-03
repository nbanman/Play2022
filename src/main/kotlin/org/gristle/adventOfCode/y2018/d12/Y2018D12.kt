package org.gristle.adventOfCode.y2018.d12

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2018D12(input: String) {
    private val lines = input.lines()

    fun solve(generations: Int, gen2: Long = 0): Long {
        var plants = lines[0].drop(15)

        val commands = lines
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
    val c = Y2018D12(readRawInput("y2018/d12"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 4110
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2650000000466
}