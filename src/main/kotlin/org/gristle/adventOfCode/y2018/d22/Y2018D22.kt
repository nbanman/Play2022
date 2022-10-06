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

    // cavern is the representation of the terrain in the cavern. First a mutable grid is created, then the 
    // apply block fills in the geologic index. You can't do it in a constructor because apart from some defaults
    // each spot is dependent on the geologic indexes of spots to the north and west.
    // Afterwards, the grid is mapped to CavernType but converting the geoIndexes to erosion levels and modding 3.
    private val cavern = MutableGrid(width * height, width) { -1L }
        .apply {
            for (i in indices) {
                this[i] = geoIndex(Coord(i % width, i / width), this)
            }
        }.mapToGrid {
            when (it.erosionLevel() % 3) {
                0L -> CavernType.ROCKY
                1L -> CavernType.WET
                else -> CavernType.NARROW
            }
        }

    private fun geoIndex(coord: Coord, cave: Grid<Long>) = when {
        coord == target || coord == Coord.ORIGIN -> 0L
        coord.y == 0 -> coord.x * 16807L
        coord.x == 0 -> coord.y * 48271L
        else -> cave[coord.west()].erosionLevel() * cave[coord.north()].erosionLevel()
    }

    private fun Long.erosionLevel() = (this + depth) % 20183

    enum class CavernType { ROCKY, WET, NARROW }

    enum class Tool { GEAR, TORCH, NEITHER }

    data class State(val pos: Coord, val tool: Tool)

    fun part1() = cavern.subGrid(Coord.ORIGIN, target).sumOf { it.ordinal }

    /**
     * For a given proposed new location and current state, provides what tool will have to be used and the
     * time that it will take to change any tool, if necessary.
     */
    private fun changeGear(pos: Coord, id: State) = when (cavern[pos]) {
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

    fun part2() = Graph
        .aStar(
            startId = State(Coord.ORIGIN, Tool.TORCH),
            heuristic = { state -> state.pos.manhattanDistance(target).toDouble() },
            defaultEdges = { state ->
                cavern
                    .getNeighborIndices(state.pos)
                    .map { neighborIndex ->
                        val neighborCoord = cavern.coordOf(neighborIndex)
                        val (newTool, weight) = changeGear(neighborCoord, state)
                        val weightMod = if (neighborCoord == target && newTool != Tool.TORCH) 7 else 0
                        Graph.Edge(State(neighborCoord, newTool), weight.toDouble() + weightMod)
                    }
            }
        ).steps()
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