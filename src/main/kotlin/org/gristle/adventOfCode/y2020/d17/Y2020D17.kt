package org.gristle.adventOfCode.y2020.d17

import org.gristle.adventOfCode.utilities.MCoord
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

class Y2020D17(input: String) {
    private val startGrid = input.toGrid()

    private fun getCubes(dimensions: Int): Int {
        var space = mutableSetOf<MCoord>()

        startGrid.mapIndexedNotNull { index, c ->
            if (c == '.') null else index
        }.forEach { index ->
            val gridCoord = startGrid.coordOf(index)
            val coordinates = (0 until dimensions).map { dim ->
                when (dim) {
                    0 -> gridCoord.x
                    1 -> startGrid.coordOf(index).y
                    else -> 0
                }
            }
            space.add(MCoord(coordinates))
        }

        val bounds = (0 until dimensions).map { dim ->
            when (dim) {
                0 -> startGrid.xIndices
                1 -> startGrid.yIndices
                else -> 0..0
            }
        }.toMutableList()

        for (i in 1..6) {
            val newSpace = mutableSetOf<MCoord>()
            (0 until dimensions).forEach { dim ->
                val bound = bounds[dim]
                bounds[dim] = (bound.first - 1)..(bound.last + 1)
            }

            val coordinates = bounds.drop(1).fold(bounds[0].toList().map { listOf(it) }) { acc, intRange ->
                acc.flatMap { intList -> intRange.map { intList + it } } // generate lists of list
            }

            coordinates.forEach { coordinate ->
                val mc = MCoord(coordinate)
                val neighbors = mc.getNeighbors().count { space.contains(it) }
                if (neighbors == 3 || (neighbors == 2 && space.contains(mc))) newSpace.add(mc)
            }

            space = newSpace
        }

        return space.size
    }

    fun part1() = getCubes(3)

    fun part2() = getCubes(4)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D17(readRawInput("y2020/d17"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 346
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1632
}