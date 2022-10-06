package org.gristle.adventOfCode.y2018.d22

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.utilities.Graph.steps

class Y2018D22(input: String) {
    
    private val pattern = Regex("""depth: (\d+)\r?\ntarget: (\d+),(\d+)""")
    
    private val gv = input.groupValues(pattern) { it.toInt() }.first()
    
    private val depth = gv[0]
    private val target = Coord(gv[1], gv[2])
    
    private val width = target.x + 201
    private val height = target.y + 201
    
    private val caveGi = MutableGrid(width * height, width) { -1L }
        .apply {
            for (i in indices) {
                val coord = Coord(i % width, i / width)
                val geologicIndex = geoIndex(coord, this)
                this[i] = geologicIndex
            }
        } as Grid<Long>
            
    private val caveRi = List(caveGi.size) { i -> erosionLevel(Coord.fromIndex(i, width), caveGi) }
    
    private val cavern = List(caveRi.size) { i ->
        when (caveRi[i] % 3) {
            0L -> CavernType.ROCKY
            1L -> CavernType.WET
            else -> CavernType.NARROW
        }
    }.toGrid(width)

    private fun geoIndex(coord: Coord, cave: Grid<Long>) = when {
        coord == target || coord == Coord(0, 0) -> 0L
        coord.y == 0 -> coord.x * 16807L
        coord.x == 0 -> coord.y * 48271L
        else -> erosionLevel(coord.west(), cave) * erosionLevel(coord.north(), cave)
    }

    private fun erosionLevel(coord: Coord, cave: Grid<Long>): Long {
        return (cave[coord] + depth) % 20183
    }

    enum class CavernType { ROCKY, WET, NARROW } 

    enum class Tool { GEAR, TORCH, NEITHER }
    
    data class Locator(val pos: Coord, val tool: Tool)

    fun part1() = cavern.subGrid(Coord.ORIGIN, target).sumOf { it.ordinal }

    private fun changeGear(pos: Coord, id: Locator) = when (cavern[pos]) {
        CavernType.ROCKY -> {
            when (cavern[id.pos]) {
                CavernType.ROCKY -> id.tool to 1
                CavernType.WET -> if (id.tool == Tool.GEAR) {
                    id.tool to 1
                } else {
                    Tool.GEAR to 8
                }
                CavernType.NARROW -> if (id.tool == Tool.TORCH) {
                    id.tool to 1
                } else {
                    Tool.TORCH to 8
                }
            }
        }
        CavernType.WET -> {
            when (cavern[id.pos]) {
                CavernType.ROCKY -> if (id.tool == Tool.GEAR) {
                    id.tool to 1
                } else {
                    Tool.GEAR to 8
                }
                CavernType.WET -> id.tool to 1
                CavernType.NARROW -> if (id.tool == Tool.NEITHER) {
                    id.tool to 1
                } else {
                    Tool.NEITHER to 8
                }
            }
        }
        CavernType.NARROW -> {
            when (cavern[id.pos]) {
                CavernType.ROCKY -> if (id.tool == Tool.TORCH) {
                    id.tool to 1
                } else {
                    Tool.TORCH to 8
                }
                CavernType.WET -> if (id.tool == Tool.NEITHER) {
                    id.tool to 1
                } else {
                    Tool.NEITHER to 8
                }
                CavernType.NARROW -> id.tool to 1
            }
        }
    }

    private val getEdges = { id: Locator ->
        cavern
            .getNeighborIndices(id.pos)
            .map {
                val pos = cavern.coordOf(it)
                val (newTool, weight) = changeGear(pos, id)
                val weightMod = if (pos == target && newTool != Tool.TORCH) 7 else 0
                Graph.Edge(Locator(pos, newTool), weight.toDouble() + weightMod)
            }
    }

    fun part2(): Int {
        val start = Locator(Coord.ORIGIN, Tool.TORCH)
        val heuristic = { id: Locator -> id.pos.manhattanDistance(target).toDouble() }
        val path = Graph.aStar(start, heuristic, defaultEdges = getEdges)
        return path.steps()
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D22(readRawInput("y2018/d22"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 5637
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 969
}