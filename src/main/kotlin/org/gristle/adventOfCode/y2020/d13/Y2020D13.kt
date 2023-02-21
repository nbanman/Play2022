package org.gristle.adventOfCode.y2020.d13

import org.gristle.adventOfCode.Day

class Y2020D13(input: String) : Day {

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

    override fun part1(): Long {
        val start = lines.first().toInt()
        val timeSequence = generateSequence(BusState(start, 0)) { (time, _) ->
            val id = buses.find { bus -> (time + 1) % bus.id == 0L }?.id ?: 0L
            BusState(time + 1, id)
        }
        return timeSequence
            .first { (_, available) -> available != 0L }
            .let { it.busId * (it.time - start) }
    }

    override fun part2() = crt(buses).let { it.id - it.offset }
}

fun main() = Day.runDay(13, 2020, Y2020D13::class) // 115, 756261495958122