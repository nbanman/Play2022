package org.gristle.adventOfCode.y2016.d4

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2016D4(input: String) {

    data class Room(val encryptedName: String, val id: Int, val checkSum: String) {
        val isReal = let {
            val mostCommon = encryptedName
                .replace("-", "") // don't count dashes
                .eachCount() // deliver size of each group of characters
                .entries // grab both key (the char) and the value (the size)
                // sort the entries by size then alphabetical
                .sortedWith(compareByDescending<Map.Entry<Char, Int>> { it.value }.thenBy { it.key })
                .take(5) // take 1st 5
                .map { it.key } // use only the char
                .joinToString("") // join them to string
            checkSum == mostCommon
        }

        private fun Char.shift() = if (this == '-') ' ' else ((code - 97 + id) % 26 + 97).toChar()

        val name: String by lazy { encryptedName.map { it.shift() }.joinToString("") }
    }

    private val rooms = input
        .groupValues("""([a-z-]+)-(\d+)\[([a-z]+)\]""")
        .map { Room(it[0], it[1].toInt(), it[2]) }
        .filter(Room::isReal)

    fun part1() = rooms.sumOf(Room::id)

    fun part2() = rooms.find { it.name == "northpole object storage" }?.id?.toString() ?: "not found"
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2016D4(readRawInput("y2016/d4"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 158835
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 993
    println("Total time: ${timer.elapsed()}ms")
}