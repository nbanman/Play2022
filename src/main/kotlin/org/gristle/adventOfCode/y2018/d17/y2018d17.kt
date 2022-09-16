package org.gristle.adventOfCode.y2018.d17

import org.gristle.adventOfCode.utilities.*

object Y2018D17 {
    private val input = readRawInput("y2018/d17")

    private data class Directive(val xRange: IntRange, val yRange: IntRange)

    private fun MutableGrid<Char>.spoutSpots() = count { it == '|' }

    private fun MutableGrid<Char>.poolSpots() = count { it == '~' }

    private fun spread(cavern: MutableGrid<Char>, spot: Int) {
        val coord = cavern.coordOf(spot)
        // Go left
        var leftMost = coord.x
        for (x in coord.x downTo 1) {
            val leftSpot = Coord(x, coord.y)
            leftMost = x
            if (cavern[Coord(leftSpot.x - 1, leftSpot.y)] !in "|."
                || cavern[Coord(leftSpot.x, leftSpot.y + 1)] !in "#~"
            ) {
                break
            }
        }
        // Go right
        var rightMost = coord.x
        for (x in coord.x until cavern.width - 1) {
            val rightSpot = Coord(x, coord.y)
            rightMost = x
            if (cavern[Coord(rightSpot.x + 1, rightSpot.y)] !in "|."
                || cavern[Coord(rightSpot.x, rightSpot.y + 1)] !in "#~") {
                break
            }
        }
        val fill = if (cavern[leftMost, coord.y + 1] in "|." || cavern[rightMost, coord.y + 1] in "|.") '|' else '~'
        for (x in leftMost..rightMost) { cavern[x, coord.y] = fill }
    }

    fun solve(): Pair<Int, Int> {
        var minY = 0
        val cavern = input
            .groupValues("""([xy])=(\d+), [xy]=(\d+)\.\.(\d+)""")
            .map { gv ->
                val gvi = gv.drop(1).map { it.toInt() }
                val xRange = if (gv[0] == "x") gvi[0]..gvi[0] else gvi[1]..gvi[2]
                val yRange = if (gv[0] == "y") gvi[0]..gvi[0] else gvi[1]..gvi[2]
                Directive(xRange, yRange)
            }.let { directives ->
                val minX = directives.map { it.xRange.first }.minOf { it }
                val maxX = directives.map { it.xRange.last }.maxOf { it }
                minY = directives.map { it.yRange.first }.minOf { it }
                val maxY = directives.map { it.yRange.last }.maxOf { it }
                val width = maxX - minX + 5
                val height = maxY + 1
                val cave = List(width * height) { i -> if (i + minX - 2 == 500) '+' else '.' }.toMutableGrid(width)
                for (directive in directives) {
                    for (x in directive.xRange) {
                        for (y in directive.yRange) {
                            cave[x - minX + 2, y] = '#'
                        }
                    }
                }
                cave
            }
        var previousSpoutSpots = 0
        var currentSpoutSpots = 0
        var previousPoolSpots = 0
        var currentPoolSpots = 1
        var round = 0
        while (currentSpoutSpots != previousSpoutSpots || currentPoolSpots != previousPoolSpots) {
            round++
            previousSpoutSpots = currentSpoutSpots
            previousPoolSpots = currentPoolSpots
            val runningWaterSpots = cavern.mapIndexedNotNull { index, spot ->
                if (spot !in "+|") null else index
            }

            for (spot in runningWaterSpots) {
                val underSpot = spot + cavern.width
                if (underSpot in cavern.indices) {
                    when {
                        cavern[underSpot] == '.' -> cavern[underSpot] = '|'
                        cavern[underSpot] in "#~" -> spread(cavern, spot)
                    }
                }
            }
            currentSpoutSpots = cavern.spoutSpots()
            currentPoolSpots = cavern.poolSpots()
        }
        return cavern.drop(minY * cavern.width).count { it in "|~" } to  
            cavern.drop(minY * cavern.width).count { it in "~" }
    }
}

fun main() {
    val time = System.nanoTime()
    val (p1, p2) = Y2018D17.solve()
    println("Part 1: $p1") // 40879
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 34693
}