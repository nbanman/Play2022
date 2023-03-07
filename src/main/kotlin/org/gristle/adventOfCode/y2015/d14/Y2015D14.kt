package org.gristle.adventOfCode.y2015.d14

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.groupValues

class Y2015D14(input: String) : Day {

    companion object {
        private const val SECONDS = 2503
    }

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

    override fun part1() = racers.maxOf { it.distanceRaced(SECONDS) }

    override fun part2(): Int {
        return (1..SECONDS)
            .fold(mutableListOf<Reindeer>()) { acc, second ->
                val maxDistance = racers.maxOf { it.distanceRaced(second) }
                acc.apply { addAll(racers.filter { it.distanceRaced(second) == maxDistance }) }
            }.eachCount()
            .maxOf { it.value }
    }
}

fun main() = Day.runDay(Y2015D14::class)

//    Class creation: 20ms
//    Part 1: 2640 (0ms)
//    Part 2: 1102 (9ms)
//    Total time: 30ms