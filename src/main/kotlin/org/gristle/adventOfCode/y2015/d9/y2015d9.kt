package org.gristle.adventOfCode.y2015.d9

import org.gristle.adventOfCode.utilities.*

object Y2015D9 {
    private val input = readRawInput("y2015/d9")

    data class Leg(val startCity: String, val endCity: String, val distance: Int)

    val legs = input
        .groupValues("""(\w+) to (\w+) = (\d+)""")
        .flatMap { gv ->
            listOf(
                Leg(gv[0], gv[1], gv[2].toInt()),
                Leg(gv[1], gv[0], gv[2].toInt())
            )
        }

    val cities = legs
        .flatMap { listOf(it.startCity, it.endCity) }
        .distinct()

    fun shortestDistance(
        legs: List<Leg>,
        remainingLegs: List<Leg>,
        remainingCities: List<String>,
        distance: Int = 0
    ): Int {
        if (remainingCities.size <= 1) return distance
        val matchingLegs = if (legs.isEmpty()) {
            remainingLegs
        } else {
            remainingLegs.filter { leg ->
                leg.startCity == legs.last().endCity && remainingCities.contains(leg.endCity)
            }
        }
        return matchingLegs.minOf { leg ->
            shortestDistance(
                legs + leg,
                (remainingLegs - leg).filter { remainingLeg ->
                    remainingCities.contains(remainingLeg.endCity) && remainingLeg.endCity != leg.endCity
                },
                remainingCities - leg.startCity,
                distance + leg.distance
            )
        }
    }

    fun longestDistance(
        legs: List<Leg>,
        remainingLegs: List<Leg>,
        remainingCities: List<String>,
        distance: Int = 0
    ): Int {
        if (remainingCities.size <= 1) return distance
        val matchingLegs = if (legs.isEmpty()) {
            remainingLegs
        } else {
            remainingLegs.filter { leg ->
                leg.startCity == legs.last().endCity && remainingCities.contains(leg.endCity)
            }
        }
        return matchingLegs.maxOf { leg ->
            longestDistance(
                legs + leg,
                (remainingLegs - leg).filter { remainingLeg ->
                    remainingCities.contains(remainingLeg.endCity) && remainingLeg.endCity != leg.endCity
                },
                remainingCities - leg.startCity,
                distance + leg.distance
            )
        }
    }

    fun part1() = shortestDistance(emptyList(), legs, cities)
    fun part2() = longestDistance(emptyList(), legs, cities)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D9.part1()} (${elapsedTime(time)}ms)") // 207
    time = System.nanoTime()
    println("Part 2: ${Y2015D9.part2()} (${elapsedTime(time)}ms)") // 804
}