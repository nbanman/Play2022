package org.gristle.adventOfCode.y2020.d13

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D13(input: String) {

    private val lines = input.lines()
    data class Bus(val id: Long, val offset: Long)

    data class BusState(val time: Int, val busId: Int)
    fun part1(): Int {
        val start = lines.first().toInt()
        val ids = lines.last().split(',').mapNotNull { it.toIntOrNull() }
        val timeSequence = generateSequence(BusState(start, 0)) { (time, _) ->
            BusState(time + 1, ids.find { (time + 1) % it == 0 } ?: 0)
        }
        return timeSequence
            .first { (_, available) -> available != 0 }
            .let { it.busId * (it.time - start) }
    }

    private fun modularInverse(ni: Long, mod: Long) =
        generateSequence(1L) { it + 1 }
            .first { (ni % mod * it) % mod == 1L }

    private fun crt(buses: List<Bus>): Bus {
        val n = buses.fold(1L) { acc, bus -> acc * bus.id }
        val bigPhase = buses.sumOf { bus ->
            val ni = n / bus.id
            bus.offset * ni * modularInverse(ni, bus.id)
        }

        return Bus(n, bigPhase % n)
    }

    fun part2(): Long {
        val buses = lines.last().split(',').mapIndexedNotNull { index, s ->
            if (s == "x") null else {
                Bus(s.toLong(), index.toLong())
            }
        }
        return crt(buses).let { it.id - it.offset }
    }

}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D13(readRawInput("y2020/d13"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 115
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 756261495958122
}