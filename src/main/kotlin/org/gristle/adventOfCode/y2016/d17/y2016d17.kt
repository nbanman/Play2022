package org.gristle.adventOfCode.y2016.d17

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.md5
import org.gristle.adventOfCode.utilities.readRawInput

// Not refactored, not terrible.
object Y2016D17 {
    private val input = readRawInput("y2016/d17")

    private val vaultRange = 0..3
    private val openRange = 'b'..'f'

    data class Room17(val coord: Coord, val passCode: String) {
        fun getNeighbors(): List<Room17> {
            val open = passCode.md5().take(4).map { it in openRange }

            val neighbors = listOf(
                Room17(Coord(coord.x - 1, coord.y), passCode + 'L'),
                Room17(Coord(coord.x + 1, coord.y), passCode + 'R'),
                Room17(Coord(coord.x, coord.y - 1), passCode + 'U'),
                Room17(Coord(coord.x, coord.y + 1), passCode + 'D')
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

    private fun getPath(locations: List<Room17>): String {

        tailrec fun gP(locations: List<Room17>): Room17? {
            val newLocations = mutableListOf<Room17>()
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

    private fun getPath3(location: Room17): Room17? {
        if (location.coord.x == 3 && location.coord.y == 3) return location
        val neighbors = location.getNeighbors()
        if (neighbors.isEmpty()) return null
        val paths = neighbors.mapNotNull { getPath3(it) }
        return paths.maxByOrNull { it.passCode.length } ?: Room17(Coord(0, 0), "Invalid")
    }

    fun part1() = getPath(listOf(Room17(Coord(0, 0), input)))

    fun part2(): Int {
        val room = getPath3(Room17(Coord(0, 0), input)) ?: Room17(Coord(0, 0), "Invalid")
        return room.passCode.length - input.length
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D17.part1()} (${elapsedTime(time)}ms)") // DDRUDLRRRD
    time = System.nanoTime()
    println("Part 2: ${Y2016D17.part2()} (${elapsedTime(time)}ms)") // 398
}