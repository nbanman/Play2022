package org.gristle.adventOfCode.y2020.d13

import org.gristle.adventOfCode.utilities.*

object Y2020D13 {
    private val input = readInput("y2020/d13")

    data class Bus(val id: Long, val offset: Long)

    fun part1(): Int {
        val start = input.first().toInt()
        val ids = input.last().split(',').mapNotNull { it.toIntOrNull() }
        var time = start
        while (true) {
            val available = ids.find { time % it == 0 }
            if (available != null) return available * (time - start)
            time++
        }
    }

    fun modularInverse(ni: Long, mod: Long): Long {
        val reducedNi = ni % mod
        var mult = 1L
        while (true) {
            if ((reducedNi * mult) % mod == 1L) return mult
            mult++
        }
    }

    fun crt(buses: List<Bus>): Bus {
        val n = buses.fold(1L) { acc, bus -> acc * bus.id }
        val bigPhase = buses
            .map { bus ->
                val ni = n / bus.id
                bus.offset * ni * modularInverse(ni, bus.id)
            }.sum()

        return Bus(n, bigPhase % n)
    }

    fun part2(): Long {
        val buses = input.last().split(',').mapIndexedNotNull { index, s ->
            if (s == "x") null else {
                Bus(s.toLong(), index.toLong())
            }
        }
        return crt(buses).let { it.id - it.offset }
    }

}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D13.part1()} (${elapsedTime(time)}ms)") // 115
    time = System.nanoTime()
    println("Part 2: ${Y2020D13.part2()} (${elapsedTime(time)}ms)") // 756261495958122
}