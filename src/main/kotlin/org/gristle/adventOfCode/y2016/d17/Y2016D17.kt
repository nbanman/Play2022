package org.gristle.adventOfCode.y2016.d17

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.md5
import org.gristle.adventOfCode.utilities.readRawInput

// Not refactored, not terrible.
class Y2016D17(private val input: String) {

    private val vaultRange = 0..3
    data class Room(val coord: Coord, val passCode: String, val vaultRange: IntRange) {

        private val openRange = 'b'..'f'
        fun getNeighbors(): List<Room> {
            val open = passCode.md5().take(4).map { it in openRange }

            val neighbors = listOf(
                Room(Coord(coord.x - 1, coord.y), passCode + 'L', vaultRange),
                Room(Coord(coord.x + 1, coord.y), passCode + 'R', vaultRange),
                Room(Coord(coord.x, coord.y - 1), passCode + 'U', vaultRange),
                Room(Coord(coord.x, coord.y + 1), passCode + 'D', vaultRange)
            ).filter {
                it.coord.x in vaultRange &&
                        it.coord.y in vaultRange &&
                        when(it.passCode.last()) {
                            'L' -> open[2]
                            'R' -> open[3]
                            'U' -> open[0]
                            else -> open[1]
                        }
            }

            return neighbors

        }
    }

    private fun getPath(locations: List<Room>): String {

        tailrec fun gP(locations: List<Room>): Room? {
            val newLocations = mutableListOf<Room>()
            locations.forEach { newLocations.addAll(it.getNeighbors()) }
            return newLocations.find { it.coord == Coord(vaultRange.last, vaultRange.last) }
                ?: if (newLocations.isEmpty()) {
                    null
                } else {
                    gP(newLocations)
                }
        }
        return gP(locations)?.passCode?.drop(locations.first().passCode.length) ?: "Invalid"

    }

    private fun getPath3(location: Room): Room? {
        if (location.coord.x == 3 && location.coord.y == 3) return location
        val neighbors = location.getNeighbors()
        if (neighbors.isEmpty()) return null
        val paths = neighbors.mapNotNull { getPath3(it) }
        return paths.maxByOrNull { it.passCode.length } ?: Room(Coord(0, 0), "Invalid", vaultRange)
    }

    fun part1() = getPath(listOf(Room(Coord(0, 0), input, vaultRange)))

    fun part2(): Int {
        val room = getPath3(Room(Coord(0, 0), input, vaultRange))
            ?: Room(Coord(0, 0), "Invalid", vaultRange)
        return room.passCode.length - input.length
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D17(readRawInput("y2016/d17"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // DDRUDLRRRD
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 398
}
