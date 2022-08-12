package org.gristle.adventOfCode.y2016.d14

import org.gristle.adventOfCode.utilities.*
import java.util.LinkedList

// Refactored!
object Y2016D14 {
    private val salt = "ngcjuoqr"

    private fun String.stretchedMd5() = (1..2017).fold(this) { acc, _ -> acc.md5() }

    private val threes = """([0-9a-f])\1{2}""".toRegex()
    private val fives = """([0-9a-f])\1{4}""".toRegex()

    private data class Marker(val index: Int, val hash: String, val three: Char?, val five: List<Char>) {
        companion object {
            fun of(index: Int, hash: String): Marker {
                val three = threes.find(hash)?.value?.get(0)
                val five = fives.findAll(hash).map { it.value[0] }.toList()
                return Marker (index, hash, three, five)
            }
        }
    }

    fun solve(hashing: (String) -> String): Int {
        // rolling list of 1,000 hashes plus useful metadata
        val hashes = LinkedList<Marker>()

        // extension function that dumps a value if the hash exceeds 1,000
        fun LinkedList<Marker>.addHash(marker: Marker) {
            add(marker)
            if (size == 1001) poll()
        }

        // set of cromulent hashes
        val counted = mutableSetOf<Marker>()

        generateSequence(0) { it + 1 }
            .onEach { index ->
                val marker = Marker.of(index, hashing(salt + index))
                marker.five.forEach { fiveChar ->
                    counted.addAll(hashes.filter { it.three == fiveChar })
                }
                hashes.addHash(marker)
            }.terminate { counted.size >= 64 }

        return counted.maxOf { it.index }
    }

    fun part1() = solve { it.md5() }

    fun part2() = solve { it.stretchedMd5() }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D14.part1()} (${elapsedTime(time)}ms)") // 18626 (124ms)
    time = System.nanoTime()
    println("Part 2: ${Y2016D14.part2()} (${elapsedTime(time)}ms)") // 20092 (11175ms)
}