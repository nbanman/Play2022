package org.gristle.adventOfCode.y2019.d10

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.IndexedHeap
import org.gristle.adventOfCode.utilities.gcd
import kotlin.math.PI
import kotlin.math.atan2

class Y2019D10(private val input: String) : Day {

    private val asteroids: List<Coord>
    private val station: Coord
    private val detectableFromStation: Int

    init {
        val width = input.takeWhile { it != '\r' }.length
        asteroids = input
            .replace("\r\n", "")
            .mapIndexedNotNull { index, c -> if (c == '.') null else Coord(index % width, index / width) }
        val (detectableFromStation, station) = asteroids
            .map { asteroid ->
                (asteroids - asteroid)
                    .map { otherAsteroid ->
                        val relativeCoord = asteroid - otherAsteroid
                        val gcd = gcd(relativeCoord.x, relativeCoord.y)
                        val new = Coord(relativeCoord.x / gcd, relativeCoord.y / gcd)
                        new
                    }.distinct()
                    .size to asteroid
            }.maxByOrNull { it.first } ?: throw Exception("no asteroids")
        this.station = station
        this.detectableFromStation = detectableFromStation
    }

    override fun part1() = detectableFromStation

    override fun part2(): Int {
        val angles = (asteroids - station)
            .map { asteroid ->
                val relativeCoord = station - asteroid
                val gcd = gcd(relativeCoord.x, relativeCoord.y)
                val new = Coord(relativeCoord.x / gcd, relativeCoord.y / gcd)
                val newAngle = atan2(new.x.toDouble(), new.y.toDouble())
                    .let { if (it <= 0.0) it else (-2 * PI) + it }
                newAngle to asteroid
            }
            .sortedBy { it.second.manhattanDistance(station) }
            .groupBy { it.first }
            .values

        val pq = IndexedHeap.maxHeap<Pair<Double, Coord>> { o1, o2 ->
            (o1.first - o2.first).let { if (it < 0.0) -1 else if (it > 0.0) 1 else 0 }
        }
        for (angle in angles) {
            angle.forEachIndexed { index, pair -> pq.add(-10.0 * index + pair.first to pair.second) }
        }

        return pq.toList()[199].let { it.second.x * 100 + it.second.y }
    }
}

fun main() = Day.runDay(Y2019D10::class)
//    var time = System.nanoTime()
//    val c = Y2019D10(readRawInput("y2019/d10"))
//    println("Class creation: ${elapsedTime(time)}ms")
//    time = System.nanoTime()
//    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 286
//    time = System.nanoTime()
//    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 504
//
//}