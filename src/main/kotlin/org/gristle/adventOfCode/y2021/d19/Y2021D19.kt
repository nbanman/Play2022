package org.gristle.adventOfCode.y2021.d19

import org.gristle.adventOfCode.utilities.*
import kotlin.math.abs

class Y2021D19(input: String) {
    companion object {
        private fun <T> List<T>.elementPairs(): Sequence<Pair<T, T>> = sequence {
            for (i in 0 until size - 1)
                for (j in i + 1 until size)
                    yield(get(i) to get(j))
        }
    }

    private val pattern = """--- scanner (\d+) ---\n((?:-?\d+,-?\d+,-?\d+\n)+)""".toRegex()

    data class Scanner(val id: String, val beacons: List<MCoord>, val scannerLocations: List<MCoord> = emptyList()) {
        
        fun MCoord.toSet(): Set<Int> = coordinates.map { abs(it) }.toSet()

        val morphisms = beacons
            .map { b ->
                listOf(
                    MCoord(b[0], b[1], b[2]),
                    MCoord(b[1], -b[0], b[2]),
                    MCoord(-b[0], -b[1], b[2]),
                    MCoord(-b[1], b[0], b[2]),
                    MCoord(b[2], b[1], -b[0]),
                    MCoord(b[2], -b[0], -b[1]),
                    MCoord(b[2], -b[1], b[0]),
                    MCoord(b[2], b[0], b[1]),
                    MCoord(-b[0], b[1], -b[2]),
                    MCoord(-b[1], -b[0], -b[2]),
                    MCoord(b[0], -b[1], -b[2]),
                    MCoord(b[1], b[0], -b[2]),
                    MCoord(-b[2], b[1], b[0]),
                    MCoord(-b[2], -b[0], b[1]),
                    MCoord(-b[2], -b[1], -b[0]),
                    MCoord(-b[2], b[0], -b[1]),
                    MCoord(b[0], b[2], -b[1]),
                    MCoord(b[1], b[2], b[0]),
                    MCoord(-b[0], b[2], b[1]),
                    MCoord(-b[1], b[2], -b[0]),
                    MCoord(b[0], -b[2], b[1]),
                    MCoord(b[1], -b[2], -b[0]),
                    MCoord(-b[0], -b[2], -b[1]),
                    MCoord(-b[1], -b[2], b[0]),
                )
            }

        var mightyMorphisms = List(morphisms.first().size) { i ->
            List(morphisms.size) { j ->
                morphisms[j][i]
            }
        }

        val coordPairs = beacons
            .indices
            .toList()
            .elementPairs()
            .toList()
            .associateBy { (beacons[it.first] - beacons[it.second]).toSet() }

        override fun toString(): String {
            return "Scanner(id=$id, beacons=${beacons.size})"
        }
    }

    private val scanners = input
        .stripCarriageReturns()
        .groupValues(pattern)
        .map { g ->
            val id = g[0]
            val beacons = g[1].dropLast(1)
                .split('\n')
                .map { line ->
                    line
                        .split(',')
                        .map { it.toInt() }
                }.map {
                    MCoord(it)
                }
            Scanner(id, beacons)
        }


    data class SharedSets(val master: Scanner, val b: Scanner, val matches: Set<Set<Int>>) {
        override fun toString(): String {
            return "SharedSets(master=$master, b=$b, sets=${matches.size})"
        }

        fun merge(): Scanner {
            // Pick the first matches and use them to align and find offset
            val matchSet = matches.first()
            val masterIndexPair = master.coordPairs[matchSet]!!
            val bIndexPair = b.coordPairs[matchSet]!!
            val m1 = master.beacons[masterIndexPair.first]
            val m2 = master.beacons[masterIndexPair.second]
            val mDiff = m1 - m2
            val bMorph = b.morphisms[bIndexPair.first] zip b.morphisms[bIndexPair.second]
            var match = bMorph
                .withIndex()
                .find { it.value.first - it.value.second == mDiff }
            if (match != null) {
                val offset = m1 - match.value.first
                val rotatedOffsetBeacons = b.mightyMorphisms[match.index].map { it + offset }
                return Scanner(
                    master.id + b.id,
                    (master.beacons + rotatedOffsetBeacons).distinct(),
                    master.scannerLocations + offset
                )
            } else {
                match = bMorph
                    .withIndex()
                    .find { it.value.second - it.value.first == mDiff }!!
                val offset = m1 - match.value.second
                val rotatedOffsetBeacons = b.mightyMorphisms[match.index].map { it + offset }
                return Scanner(
                    master.id + b.id,
                    (master.beacons + rotatedOffsetBeacons).distinct(),
                    master.scannerLocations + offset
                )
            }

        }
    }

    fun solve(): Pair<Int, Int> {

        var master = scanners.first()
        val mScan = scanners.drop(1).toMutableList()

        while (mScan.isNotEmpty()) {

            val sharedSet = mScan
                .map {
                    SharedSets(master, it, master.coordPairs.keys.intersect(it.coordPairs.keys))
                }.first { it.matches.size >= 66 }

            master = sharedSet.merge()
            mScan.remove(sharedSet.b)
        }

        val furthestDistance = master.scannerLocations.elementPairs().toList().maxOf { it.first.manhattanDistance(it.second) }

        return master.beacons.size to furthestDistance
    }
}
    
fun main() {
    val time = System.nanoTime()
    val (p1, p2) = Y2021D19(readStrippedInput("y2021/d19")).solve()
    println("Part 1: $p1") // 378
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 13148
}