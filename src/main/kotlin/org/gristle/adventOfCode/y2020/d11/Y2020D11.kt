package org.gristle.adventOfCode.y2020.d11

import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

class Y2020D11(input: String) {

    private val layout = input.toGrid()
    private fun stabilize(tolerance: Int, getNeighbors: (Grid<Char>, Int) -> Int): Int {
        var mLayout = layout.toGrid()
        var changed: Boolean
        do {
            changed = false
            val nLayout = List(mLayout.size) { i ->
                val seat = mLayout[i]
                if (seat == '.') {
                    '.'
                } else {
                    val occupied = seat == '#'
                    val neighbors = getNeighbors(mLayout, i)
                    val nSeat = if ((occupied && neighbors < tolerance) || (!occupied && neighbors == 0)) '#' else 'L'
                    if (!changed) changed = seat != nSeat
                    nSeat
                }
            }.toGrid(mLayout.width)
//            println(nLayout.representation { it })
            if (!changed) return mLayout.count { it == '#' } else mLayout = nLayout
        } while (true)
    }

    fun part1() = stabilize(4) { grid, i ->
        grid.getNeighbors(i, true).count { it == '#' }
    }

    fun part2() = stabilize(5) { grid, i ->
        val coord = grid.coordOf(i)
        grid
            .getNeighborIndices(i, true)
            .map { neighborIndex ->
                val slope = grid.coordOf(neighborIndex) - coord
                var seesOccupied = false
                var newCoord = coord + slope
                while (grid.validCoord(newCoord) && !seesOccupied) {
                    if (grid[newCoord] == '#') seesOccupied = true
                    if (grid[newCoord] == 'L') break
                    newCoord += slope
                }
                seesOccupied
            }.count { it }
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D11(readRawInput("y2020/d11"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 2243
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2027
}