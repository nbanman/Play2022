package org.gristle.adventOfCode.y2018.d22

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.utilities.Graph.steps
class Y2018D22(input: String) {

    private val pattern = Regex("""depth: (\d+)\r?\ntarget: (\d+),(\d+)""")

    private val gv = input.groupValues(pattern, String::toInt).first()

    private val depth = gv[0]
    private val start = Coord.ORIGIN
    private val target = Coord(gv[1], gv[2])

    // the type of terrain found in each location of the cavern
    enum class Terrain { ROCKY, WET, NARROW }

    /**
     * Object used to provide the terrain of any position in the cavern. Internally it uses a mutable map to hold
     * geologic index values. These values are calculated lazily upon request. Prior implementation used a Grid
     * that calculated all values in advance. This was faster, but was a less general solution because the Grid
     * required padding on the right and bottom to allow the explorer to overshoot the target in search of more
     * efficient terrain to navigate. This version is theoretically limited only by the number of elements allowed
     * in a Map object.
     */
    class Cavern(start: Coord, target: Coord, private val depth: Int) {
        // helper function used to convert geologic index to erosion level
        private fun Long.erosionLevel() = (this + depth) % 20183

        // helper function used to convert geologic index to terrain 
        private fun Long.terrain() = when (erosionLevel() % 3) {
            0L -> Terrain.ROCKY
            1L -> Terrain.WET
            else -> Terrain.NARROW
        }

        // map to hold the geologic index of the cavern. From this, the terrain for any location can be
        // calculated. It remains mutable because the values are calculated lazily as needed using the geoIndex
        // function. It starts with 0 at both start and target, per the instructions.
        private val geoIndexMap = mutableMapOf<Coord, Long>().apply {
            put(start, 0)
            put(target, 0)
        }

        private fun geoIndex(pos: Coord): Long {
            return geoIndexMap[pos] ?: let { // if value stored in map, return value. Otherwise...
                geoIndexMap[pos] = when { // ...calculate value and assign it to the map using provided rules
                    pos.y == 0 -> pos.x * 16807L
                    pos.x == 0 -> pos.y * 48271L
                    else -> geoIndex(pos.west()).erosionLevel() * geoIndex(pos.north()).erosionLevel() // recursive!
                }
                geoIndexMap.getValue(pos) // provide the newly assigned value
            }
        }

        // sole public getter provides the terrain for a given position
        operator fun get(pos: Coord): Terrain = geoIndex(pos).terrain()
    }

    // initialize the cavern
    private val cavern = Cavern(start, target, depth)

    // for each position in the rectangle formed by the start and the target, add up the risk level
    fun part1() = Coord.rectangleFrom(start, target).sumOf { cavern[it].ordinal }

    enum class Tool { GEAR, TORCH, NEITHER }

    data class State(val pos: Coord, val tool: Tool)

    /**
     * For a given proposed new location and current state, provides what tool will have to be used and the
     * time that it will take to change any tool, if necessary.
     */
    private fun changeTool(neighborTerrain: Terrain, state: State): Tool {
        val stateTerrain = cavern[state.pos]
        // if terrain is the same, no tool change needed and the weight is 1
        return if (stateTerrain == neighborTerrain) {
            state.tool
        } else {
            // if terrain is different, provide the tool that works for those two terrains by adding the ordinal values of the terrain
            // together to obtain a unique value specific to that terrain combination.
            when (stateTerrain.ordinal + neighborTerrain.ordinal) {
                1 -> Tool.GEAR
                2 -> Tool.TORCH
                3 -> Tool.NEITHER
                else -> throw IllegalArgumentException("Terrain ordinals did not add up correctly")
            }
        }
    }

    /**
     * Run A* algorithm to find the shortest path, using a State object that tracks both the position and the current
     * tool. The heuristic is the manhattan distance to the target. Edges are found by looking at neighboring
     * positions and calculating what tool change, if necessary, needs to be made.
     */
    fun part2() = Graph
        .aStar(
            startId = State(start, Tool.TORCH),
            heuristic = { state -> state.pos.manhattanDistance(target).toDouble() },
            defaultEdges = { state ->
                state.pos
                    .getNeighbors() // get four neighbors of the location as grid indices
                    .filter { it.x >= 0 && it.y >= 0 } // avoid negative positions
                    .map { neighbor -> // for each neighbor index...
                        val newTool = changeTool(cavern[neighbor], state) // ...get the new tool if necessary 
                        val weight = if (state.tool != newTool) 8.0 else 1.0 // calculate weight incl tool change
                        // add 7 weight if the neighbor is the target and the target is not Torch, to account for
                        // ending up as torch
                        val endMod = if (neighbor == target && newTool != Tool.TORCH) 7 else 0
                        // wrap together in an Edge object
                        Graph.Edge(State(neighbor, newTool), weight + endMod)
                    }
            }
        ).steps()
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D22(readRawInput("y2018/d22"))
    println("Class creation: ${elapsedTime(time)}ms") // (35ms) (98ms grid)
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 5637 (27ms) (1ms grid)
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 969 (1057ms) (491ms grid)
}