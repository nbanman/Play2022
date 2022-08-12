package org.gristle.adventOfCode.y2019.d10

import org.gristle.adventOfCode.utilities.*
import java.util.PriorityQueue
import kotlin.math.PI
import kotlin.math.atan2

object Y2019D10 {
    private val input = readRawInput("y2019/d10")

    fun solve(): Pair<Int, Int> {
        val width = input.takeWhile { it != '\r' }.length
        val asteroids = input
            .replace("\r\n", "")
            .mapIndexedNotNull { index, c -> if (c == '.') null else Coord(index % width, index / width) }
        val p1 = asteroids.map { asteroid ->
            (asteroids - asteroid)
                .map { otherAsteroid ->
                    val relativeCoord = asteroid - otherAsteroid
                    val gcd = gcd(relativeCoord.x, relativeCoord.y)
                    val new = Coord(relativeCoord.x / gcd, relativeCoord.y / gcd)
                    new
                }.distinct()
                .size to asteroid
        }.maxByOrNull { it.first }!!

        //Part 2
        val station = p1.second
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

        val pq = IndexedHeap.maxHeap<Pair<Double, Coord>>(Comparator {
                o1, o2 -> (o1.first - o2.first).let { if(it < 0.0) -1 else if (it > 0.0) 1 else 0 }
        })
        for (angle in angles.keys) {
            angles[angle]!!.forEachIndexed { index, pair -> pq.add(-10.0 * index + pair.first to pair.second) }
        }
        val p2 = pq.dumpToList()[199].let { it.second.x * 100 + it.second.y }
        
        return p1.first to p2
    }
   
}

fun main() {
    val time = System.nanoTime()
    val (p1, p2) = Y2019D10.solve()
    println("Part 1: $p1") // 286
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 504 
}