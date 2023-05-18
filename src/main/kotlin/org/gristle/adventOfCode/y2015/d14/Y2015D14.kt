package org.gristle.adventOfCode.y2015.d14

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts

class Y2015D14(input: String) : Day {

    companion object {
        private const val SECONDS = 2503
    }

    data class Reindeer(val speed: Int, val duration: Int, val rest: Int) {
        fun distanceRaced(seconds: Int): Int {
            val interval = duration + rest
            val wholeIntervals = seconds / interval
            val remainder = seconds % interval
            return wholeIntervals * (speed * duration) + minOf(remainder, duration) * speed
        }
    }

    private val racers = input
        .getInts()
        .chunked(3)
        .map { (speed, duration, rest) -> Reindeer(speed, duration, rest) }
        .toList()

    override fun part1() = racers.maxOf { it.distanceRaced(SECONDS) }

    override fun part2(): Int {
        val leaderboard = IntArray(racers.size)
        for (t in 1..SECONDS) {
            val distances = racers.map { it.distanceRaced(t) }
            val maxDistance = distances.max()
            distances.forEachIndexed { racer, distance -> if (distance == maxDistance) leaderboard[racer]++ }
        }
        return leaderboard.max()
    }
}

fun main() = Day.runDay(Y2015D14::class)

//    Class creation: 14ms
//    Part 1: 2640 (0ms)
//    Part 2: 1102 (8ms)
//    Total time: 23ms