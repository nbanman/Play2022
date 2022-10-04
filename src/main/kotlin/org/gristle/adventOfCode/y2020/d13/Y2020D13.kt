package org.gristle.adventOfCode.y2020.d13

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2020D13(input: String) {

    private val lines = input.lines()
    data class Bus(val id: Long, val offset: Long)

    fun part1(): Int {
        val start = lines.first().toInt()
        val ids = lines.last().split(',').mapNotNull { it.toIntOrNull() }
        var time = start
        while (true) {
            val available = ids.find { time % it == 0 }
            if (available != null) return available * (time - start)
            time++
        }
    }

    private fun modularInverse(ni: Long, mod: Long): Long {
        val reducedNi = ni % mod
        var mult = 1L
        while (true) {
            if ((reducedNi * mult) % mod == 1L) return mult
            mult++
        }
    }

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