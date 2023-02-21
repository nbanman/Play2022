package org.gristle.adventOfCode.y2016.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.groupValues

class Y2016D4(input: String) : Day {

    data class Room(val encryptedName: String, val id: Int, val checkSum: String) {
        val isReal = checkSum == encryptedName
            .replace("-", "") // don't count dashes
            .eachCount() // deliver size of each group of characters
            .entries // grab both key (the char) and the value (the size)
            // sort the entries by size then alphabetical
            .sortedWith(compareByDescending<Map.Entry<Char, Int>> { it.value }.thenBy { it.key })
            .take(5) // take 1st 5
            .map { it.key } // use only the char
            .joinToString("") // join them to string

        private fun Char.shift() = if (this == '-') ' ' else ((code - 97 + id) % 26 + 97).toChar()

        val name: String by lazy { encryptedName.map { it.shift() }.joinToString("") }
    }

    private val rooms = input
        .groupValues("""([a-z-]+)-(\d+)\[([a-z]+)\]""")
        .map { Room(it[0], it[1].toInt(), it[2]) }
        .filter(Room::isReal)

    override fun part1() = rooms.sumOf(Room::id)

    override fun part2() = rooms.find { it.name == "northpole object storage" }?.id?.toString() ?: "not found"
}

fun main() = Day.runDay(4, 2016, Y2016D4::class) // 158835, 993