package org.gristle.adventOfCode.y2018.d20

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.utilities.Graph.steps
import java.util.*

class Y2018D20(private val input: String) : Day {

    private fun makeMap(): Grid<Char> {
        val length = input.length / 2 + 20
        val map = MutableGrid(length, length) { '^' }

        fun move(coord: Coord, c: Char): Coord {
            if (c !in "NSEW") return coord
            val (doorCoord, doorSymbol, newCoord) = when (c) {
                'N' -> Triple(coord.north(), '-', coord.north(2))
                'W' -> Triple(coord.west(), '|', coord.west(2))
                'E' -> Triple(coord.east(), '|', coord.east(2))
                'S' -> Triple(coord.south(), '-', coord.south(2))
                else -> throw IllegalArgumentException()
            }
            map[doorCoord] = doorSymbol
            return newCoord
        }

        fun decorate(coord: Coord) {
            if (map[coord] != 'X') map[coord] = '.'
            val neighbors = map.getNeighborIndices(coord, false)
            val diagonals = map.getNeighborIndices(coord, true) - neighbors.toSet()
            diagonals.forEach { map[it] = '#' }
            neighbors.filter { map[it] !in "#-|.X" }.forEach { map[it] = '?' }
        }

        fun exploreMap(regex: String, start: Coord) {
            val returnCoords = ArrayDeque<Coord>()
            var i = 0
            var coord = start
            map[start] = 'X'
            while (i < regex.length - 1) {
                when (val c = regex[i]) {
                    '(' -> returnCoords.add(coord)
                    '|' -> coord = returnCoords.last
                    ')' -> returnCoords.removeLast()
                    else -> {
                        coord = move(coord, c)
                        decorate(coord)
                    }
                }
                i++
            }
        }

        exploreMap(input, Coord(length / 2, length / 2))

        val topLeft = map.coordOfElement('#')
        val bottomRight = map.lastCoordOfElement('#')
        val gridSizes = bottomRight - topLeft

        return map
            .subGrid(topLeft, gridSizes.x + 1, gridSizes.y + 1)
            .let { m -> m.mapToGrid { if (it == '?') '#' else it } }
    }

    private val area = makeMap()
    private val distances = Graph.bfs(area.coordOfElement('X')) { coord ->
        area.getNeighborIndices(coord)
            .filter { area[it] != '#' }
            .map { area.coordOf(it) }
    }

    override fun part1() = distances.steps() / 2
    override fun part2() = distances.count { area[it.id] == '.' && it.weight >= 2000 }
}

fun main() = Day.runDay(Y2018D20::class)

//    Class creation: 1729ms
//    Part 1: 3930 (0ms)
//    Part 2: 8240 (8ms)
//    Total time: 1738ms