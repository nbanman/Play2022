package org.gristle.adventOfCode.y2018.d20

import org.gristle.adventOfCode.utilities.*
import java.util.*

object Y2018D20 {
    private val data = readRawInput("y2018/d20")

    fun solve(): Pair<Int, Int> {
        val myMap = makeMap()
        val d = Graph.bfs(myMap.coordIndexOf('X')) { coord ->
            myMap.getNeighborIndices(coord)
                .filter { myMap[it] != '#' }
                .map { myMap.coordIndex(it) }
        }
            val distances = MutableList<Int?>(myMap.size) { null }
            d.forEach { v ->
                distances[myMap.indexOf(v.id)] = v.weight.toInt()
            }

        val p1 = distances.maxOf { it?.div(2) ?: 0 }

        val p2 = distances
            .filterIndexed { index, i ->
                myMap[index] in "X." && i?.let { it >= 2000 } ?: false
            }.size
        return p1 to p2
    }

    private fun makeMap(): Grid<Char> {
        val width = data.length / 2 + 20
        val map = MutableList(width * width) { '^' }.toMutableGrid(width)

        fun move(coord: Coord, c: Char): Coord {
            val (doorCoord, doorSymbol, newCoord) = when (c) {
                'N' -> Triple(coord.north(), '-', coord.north(2))
                'W' -> Triple(coord.west(), '|', coord.west(2))
                'E' -> Triple(coord.east(), '|', coord.east(2))
                'S' -> Triple(coord.south(), '-', coord.south(2))
                else -> Triple(coord, 'Z', coord)
            }
            if (doorSymbol != 'Z') {
                map[doorCoord] = doorSymbol
            }
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

        exploreMap(data, Coord(width / 2, width / 2))

        val topLeft = map.coordIndexOf('#')
        val bottomRight = map.lastCoordIndexOf('#')
        val gridSizes = bottomRight - topLeft

        val returnMap = map
            .subGrid(topLeft, gridSizes.x + 1, gridSizes.y + 1)
            .let { m ->
                m.map { if (it == '?') '#' else it }.toGrid(m.width)
            }

        return returnMap
    }
    
}

fun main() {
    val time = System.nanoTime()
    val (p1, p2) = Y2018D20.solve()
    println("Part 1: $p1") // 3930
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 8240 
}