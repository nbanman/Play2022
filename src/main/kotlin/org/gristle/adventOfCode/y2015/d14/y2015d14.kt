package org.gristle.adventOfCode.y2015.d14

import org.gristle.adventOfCode.utilities.*

object Y2015D14 {
    private val input = readRawInput("y2015/d14")

    data class Reindeer(val name: String, val speed: Int, val duration: Int, val rest: Int) {
        fun distanceRaced(seconds: Int): Int {
            val interval = duration + rest
            val wholeIntervals = seconds / interval
            val remainder = seconds % interval
            return wholeIntervals * (speed * duration) + minOf(remainder, duration) * speed
        }
    }

    private val racers = input
        .groupValues("""(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\.""")
        .map { gv -> Reindeer(gv[0], gv[1].toInt(), gv[2].toInt(), gv[3].toInt()) }

    private const val SECONDS = 2503

    fun part1() = racers.maxOfOrNull { it.distanceRaced(SECONDS) } ?: 0

    fun part2(): Int {
        return (1..SECONDS).fold(mutableListOf<Reindeer>()) { acc, second ->
                val maxDistance = racers.maxOf { it.distanceRaced(second) }
                acc.apply { addAll(racers.filter { it.distanceRaced(second) == maxDistance }) }
            }.eachCount()
            .maxBy { it.value }
            .value
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D14.part1()} (${elapsedTime(time)}ms)") // 2640
    time = System.nanoTime()
    println("Part 2: ${Y2015D14.part2()} (${elapsedTime(time)}ms)") // 1102
}