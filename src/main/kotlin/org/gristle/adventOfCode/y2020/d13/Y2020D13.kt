package org.gristle.adventOfCode.y2020.d13

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D13(input: String) {

    private val lines = input.lines()
    data class Bus(val id: Long, val offset: Long)
    data class BusState(val time: Int, val busId: Long)
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

    private val buses = lines.last().split(',').mapIndexedNotNull { index, s ->
        if (s == "x") null else {
            Bus(s.toLong(), index.toLong())
        }
    }

    fun part1(): Long {
        val start = lines.first().toInt()
        val timeSequence = generateSequence(BusState(start, 0)) { (time, _) ->
            val id = buses.find { bus -> (time + 1) % bus.id == 0L }?.id ?: 0L
            BusState(time + 1, id)
        }
        return timeSequence
            .first { (_, available) -> available != 0L }
            .let { it.busId * (it.time - start) }
    }

    fun part2() = crt(buses).let { it.id - it.offset }
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