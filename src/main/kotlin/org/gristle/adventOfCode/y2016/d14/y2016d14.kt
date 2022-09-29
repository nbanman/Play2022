package org.gristle.adventOfCode.y2016.d14

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.md5
import java.util.*

// Refactored!
object Y2016D14 {
    private const val SALT = "ngcjuoqr"

    private fun String.stretchedMd5() = (1..2017).fold(this) { acc, _ -> acc.md5() }

    private val threes = """([\da-f])\1{2}""".toRegex()
    private val fives = """([\da-f])\1{4}""".toRegex()

    private data class Marker(val index: Int, val hash: String, val three: Char?, val fives: List<Char>) {
        companion object {
            fun of(index: Int, hash: String): Marker {
                val three = threes.find(hash)?.value?.get(0)
                val five = fives.findAll(hash).map { it.value[0] }.toList()
                return Marker(index, hash, three, five)
            }
        }
    }

    fun solve(hashing: (String) -> String): Int {
        // rolling list of 1,000 hashes plus useful metadata
        val markers = ArrayDeque<Marker>(1000)

        // extension function that dumps a value if the hash exceeds 1,000
        fun Deque<Marker>.addMarker(marker: Marker) {
            if (size == 1000) poll()
            add(marker)
        }

        // set of cromulent hashes
        val counted = mutableSetOf<Marker>()

        // this generates hashes for each index, storing them in a rolling list of 1,000 hashes. When fives are
        // encountered, it looks through the list and adds the hashes with a corresponding three to the count set.
        // The sequence continues for 1,000 iterations after the 64th cromulent hash is added. This is because 
        // hashes do not get added sequentially, but when their corresponding five is called. However, after 1,000
        // calls, there's no chance that it will ever be added because it's been rolled off.
        generateSequence(0) { it + 1 }
            .onEach { index ->
                val marker = Marker.of(index, hashing(SALT + index))
                marker.fives.forEach { fiveChar ->
                    counted.addAll(markers.filter { it.three == fiveChar })
                }
                markers.addMarker(marker)
            }.first { index -> counted.size >= 64 && index >= counted.elementAt(63).index + 1000 }
        
        // Sort by index, then return the index of the 64th hash.
        return counted.sortedBy { it.index }[63].index
    }

    fun part1() = solve { it.md5() }

    fun part2() = solve { it.stretchedMd5() }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D14.part1()} (${elapsedTime(time)}ms)") // 18626 (124ms)
    time = System.nanoTime()
    println("Part 2: ${Y2016D14.part2()} (${elapsedTime(time)}ms)") // 20092 (11571ms)
}