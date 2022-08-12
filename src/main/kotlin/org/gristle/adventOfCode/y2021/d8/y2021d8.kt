package org.gristle.adventOfCode.y2021.d8

import org.gristle.adventOfCode.utilities.*

object Y2021D8 {
    private val input = readInput("y2021/d8")

    data class Display(val wires: List<Set<Char>>, val display: List<Set<Char>>) {
        val mapping = mutableMapOf<Set<Char>, Int>().apply {
            val helperMap = mutableMapOf<Int, Set<Char>>()
            val wireGroups = wires.groupBy { it.size }
            wireGroups.filter { it.value.size == 1 }.forEach { entry ->
                when (entry.key) {
                    2 -> {
                        val wireSet = entry.value.first()
                        put(wireSet, 1)
                        helperMap[1] = wireSet
                    }
                    3 -> {
                        val wireSet = entry.value.first()
                        put(wireSet, 7)
                        helperMap[7] = wireSet
                    }
                    4 -> {
                        val wireSet = entry.value.first()
                        put(wireSet, 4)
                        helperMap[4] = wireSet
                    }
                    7 -> {
                        val wireSet = entry.value.first()
                        put(wireSet, 8)
                        helperMap[8] = wireSet
                    }
                }
            }
            wireGroups[6]!!.forEach { six ->
                when { //TODO this ordering needs testing
                    (six - helperMap[1]!!).size == 5 -> {
                        put(six, 6)
                        helperMap[6] = six
                    }
                    (six - helperMap[4]!!).size == 2  -> {
                        put(six, 9)
                        helperMap[9] = six
                    }
                    (six - helperMap[4]!!).size == 3  -> {
                        put(six, 0)
                        helperMap[0] = six
                    }
                }
            }

            wireGroups[5]!!.forEach { five ->
                when {
                    (helperMap[6]!! - five).size == 1 -> put(five, 5)
                    (five - helperMap[4]!!).size == 3 -> put(five, 2)
                    else -> put(five, 3)
                }
            }
        }.toMap()

        val translatedDisplay = display.map { mapping[it]!! }

        val readout = translatedDisplay.joinToString("").toInt()
    }

    val displays = input
        .map { line ->
            val (wireString, displayString) = line.split(" | ")
            val wires = wireString.split(' ').map { it.toSet() }
            val display = displayString.split(' ').map { it.toSet() }
            Display(wires, display)
        }

    fun part1() = displays.flatMap { it.translatedDisplay }.count { it == 1 || it == 4 || it == 7 || it == 8 }

    fun part2() = displays.sumOf { it.readout }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D8.part1()} (${elapsedTime(time)}ms)") // 397
    time = System.nanoTime()
    println("Part 2: ${Y2021D8.part2()} (${elapsedTime(time)}ms)") // 1027422
}