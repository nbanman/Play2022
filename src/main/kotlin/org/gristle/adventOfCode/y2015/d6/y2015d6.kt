package org.gristle.adventOfCode.y2015.d6

import org.gristle.adventOfCode.utilities.*
import kotlin.math.max

object Y2015D6 {
    private val input = readRawInput("y2015/d6")

    private const val pattern = """(turn (?:on|off)|toggle) (\d+),(\d+) through (\d+),(\d+)"""

    enum class Mode { ON, OFF, TOGGLE }

    data class Instruction(val mode: Mode, val tl: Coord, val br: Coord) {

        fun execute(lights: MutableList<Boolean>) {
            when (mode) {
                Mode.ON -> {
                    for (y in tl.y..br.y) {
                        for (x in tl.x..br.x) {
                            lights[y * 1000 + x] = true
                        }
                    }
                }
                Mode.OFF -> {
                    for (y in tl.y..br.y) {
                        for (x in tl.x..br.x) {
                            lights[y * 1000 + x] = false
                        }
                    }
                }
                Mode.TOGGLE -> {
                    for (y in tl.y..br.y) {
                        for (x in tl.x..br.x) {
                            val newCoord = y * 1000 + x
                            lights[newCoord] = !lights[newCoord]
                        }
                    }
                }
            }
        }

        fun executeDimmer(lights: MutableList<Int>) {
            when (mode) {
                Mode.ON -> {
                    for (y in tl.y..br.y) {
                        for (x in tl.x..br.x) {
                            lights[y * 1000 + x]++
                        }
                    }
                }
                Mode.OFF -> {
                    for (y in tl.y..br.y) {
                        for (x in tl.x..br.x) {
                            lights[y * 1000 + x] = max(0, lights[y * 1000 + x] - 1)
                        }
                    }
                }
                Mode.TOGGLE -> {
                    for (y in tl.y..br.y) {
                        for (x in tl.x..br.x) {
                            lights[y * 1000 + x] += 2
                        }
                    }
                }
            }
        }
    }

    private val instructions = input
        .groupValues(pattern)
        .map { gv ->
            val coords = gv.drop(1).map { it.toInt() }
            Instruction(
                when (gv[0]) {
                    "turn on" -> Mode.ON
                    "turn off" -> Mode.OFF
                    else -> Mode.TOGGLE
                },
                Coord(coords[0], coords[1]),
                Coord(coords[2], coords[3])
            )
        }

    fun part1(): Int {
        val lights = MutableList(1_000_000) { false }
        instructions.forEach { it.execute(lights) }
        return lights.count { it }
    }

    fun part2(): Int {
        val lights = MutableList(1_000_000) { 0 }
        instructions.forEach { it.executeDimmer(lights) }
        return lights.sum()
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D6.part1()} (${elapsedTime(time)}ms)") // 569999
    time = System.nanoTime()
    println("Part 2: ${Y2015D6.part2()} (${elapsedTime(time)}ms)") // 17836115
}