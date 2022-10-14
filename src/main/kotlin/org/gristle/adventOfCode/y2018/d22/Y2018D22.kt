package org.gristle.adventOfCode.y2018.d22

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.utilities.Graph.steps

class Y2018D22(input: String) {

    private val pattern = Regex("""depth: (\d+)\r?\ntarget: (\d+),(\d+)""")

    private val gv = input.groupValues(pattern, String::toInt).first()

    private val depth = gv[0]
    private val target = Coord(gv[1], gv[2])

    // the cavern size has some padding so that an explorer can "overshoot" the target and head back if that ends
    // up being faster due to circumventing difficult terrain patterns.
    private val width = target.x + 201
    private val height = target.y + 201

    // obtains geologic index value from the cavern index and the cavern itself
    private fun geoIndex(coord: Coord, cave: Grid<Long>) = when {
        coord == target || coord == Coord.ORIGIN -> 0L
        coord.y == 0 -> coord.x * 16807L
        coord.x == 0 -> coord.y * 48271L
        else -> cave[coord.west()].erosionLevel() * cave[coord.north()].erosionLevel()
    }

    // helper function used to convert geologic index to erosion level
    private fun Long.erosionLevel() = (this + depth) % 20183

    // the type of terrain found in each location of the cavern
    enum class CavernType { ROCKY, WET, NARROW }

    // cavern is the representation of the terrain in the cavern. First a mutable grid is created, then the 
    // apply block fills in the geologic index. You can't do it in a constructor because apart from some defaults
    // each spot is dependent in a bootstrapping fashion on the previously calculated  geologic indexes of spots 
    // to the north and west. Afterwards, the grid is mapped to CavernType by converting the geoIndexes to erosion 
    // levels and modding 3.
    private val cavern = MutableGrid(width, height) { -1L }
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

    fun part1() = cavern.subGrid(Coord.ORIGIN, target).sumOf(CavernType::ordinal)

    enum class Tool { GEAR, TORCH, NEITHER }

    data class State(val pos: Coord, val tool: Tool)

    /**
     * For a given proposed new location and current state, provides what tool will have to be used and the
     * time that it will take to change any tool, if necessary.
     */
    private fun changeGear(neighborPos: Coord, state: State): Pair<Tool, Int> {
        val stateTerrain = cavern[state.pos]
        val neighborTerrain = cavern[neighborPos]
        // if terrain is the same, no tool change needed and the weight is 1
        return if (stateTerrain == neighborTerrain) {
            state.tool to 1
        } else {
            // if terrain is different, provide the tool that works for those two terrains by adding the ordinal values of the terrain
            // together to obtain a unique value specific to that terrain combination.
            val tool = when (stateTerrain.ordinal + neighborTerrain.ordinal) {
                1 -> Tool.GEAR
                2 -> Tool.TORCH
                3 -> Tool.NEITHER
                else -> throw IllegalArgumentException("Terrain ordinals did not add up correctly")
            }
            // return this tool. If the tool as the same as the one in the state, weight is 1, else 7 to accomodate
            // gear change.
            tool to if (state.tool == tool) 1 else 8
        }
    }

    /**
     * Run A* algorithm to find shortest path, using a State object that tracks both the position and the current tool.
     * The heuristic is the manhattan distance to the target. Edges are found by looking at neighboring positions
     * and calculating what tool change, if necessary, needs to be made.
     */
    fun part2() = Graph
        .aStar(
            startId = State(Coord.ORIGIN, Tool.TORCH),
            heuristic = { state -> state.pos.manhattanDistance(target).toDouble() },
            defaultEdges = { state ->
                cavern
                    .getNeighborIndices(state.pos)
                    .map { neighborIndex ->
                        val neighborCoord = cavern.coordOf(neighborIndex)
                        val (tool, weight) = changeGear(neighborCoord, state)
                        val weightMod = if (neighborCoord == target && tool != Tool.TORCH) 7 else 0
                        Graph.Edge(State(neighborCoord, tool), weight.toDouble() + weightMod)
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