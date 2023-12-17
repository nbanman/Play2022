package org.gristle.adventOfCode.y2023.d17

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.toGrid

class Y2023D17(input: String) : Day {

    // convert input string to Grid of heat loss values. I use Double because my standard A* library function only
    // works in Double. 
    private val city = input.toGrid { it.digitToInt().toDouble() }

    // State tracking current position, direction, and how long crucible has been going straight.
    private data class State(val pos: Coord, val dir: Nsew, val straights: Int)
    
    // Start, end, and simple manhattan distance heuristic used for both parts.
    private val start = State(Coord.ORIGIN, Nsew.EAST, 0)
    private val end = city.coordOf(city.lastIndex)
    private val heuristic = { state: State -> state.pos.manhattanDistance(end).toDouble() }

    override fun part1(): Int {
        
        // Utility function for getEdges.
        // Generates next move in a particular direction, or null if the move goes out of range of the city Grid.
        fun moveOrNull(state: State, turn: Nsew.() -> Nsew): Graph.Edge<State>? {
            val turnDir = state.dir.turn()
            val pos = state.pos.move(turnDir)
            val weight = city.getOrNull(pos) ?: return null
            val straights = if (state.dir == turnDir) state.straights + 1 else 1
            return Graph.Edge(State(pos, turnDir, straights), weight)
        }
        
        // Get edges for any given state.
        val getEdges = { state: State -> 
            buildList {
                // continue straight if haven't already gone straight 3 times
                if (state.straights < 3) {
                    moveOrNull(state) { this }?.let { add(it) }
                }
                // go left and right
                moveOrNull(state, Nsew::left)?.let { add(it) }
                moveOrNull(state, Nsew::right)?.let { add(it) }
            }
        }
        
        // Run A* with the edgefinder and return the shortest number of steps.
        return Graph.aStar(start, heuristic, defaultEdges = getEdges).steps()
    }
    
    override fun part2(): Int {

        // Utility function for getEdges.
        // Generates next move in a particular direction, or null if the move goes out of range of the city Grid.
        fun moveOrNull(state: State, turn: Nsew.() -> Nsew, distance: Int): Graph.Edge<State>? {
            val turnDir = state.dir.turn()
            return if (distance == 0) null else {
                (1..distance).fold(state.pos to 0.0) { (currentPos, heatLoss), _ ->
                    val pos = currentPos.move(turnDir)
                    val weight = heatLoss + (city.getOrNull(pos) ?: return null)
                    pos to weight
                }.let { (pos, weight) ->
                    val straights = if (state.dir == turnDir) state.straights + distance else distance
                    Graph.Edge(State(pos, turnDir, straights), weight)
                }
            }
        }

        // Get edges for any given state.
        val getEdges = { state: State ->
            buildList { 
                
                // go straight
                // we can only continue straight if we haven't gone 10 times already. Also, when we start we 
                // haven't moved at all so we are permitted to move 4 in that direction.
                val distance = when (state.straights) {
                    0 -> 4 // this only happens at start
                    in 4..9 -> 1 // we have already turned and gone 4 in that direction, so add 1 up to 10
                    else -> 0 // we have already moved 10 times; go no further
                }
                moveOrNull(state, { this }, distance)?.let { add(it) }
                
                // go left and right
                moveOrNull(state, Nsew::left, 4)?.let { add(it) }
                moveOrNull(state, Nsew::right, 4)?.let { add(it) }
            }
        }

        // Run A* with the edgefinder and return the shortest number of steps.
        return Graph.aStar(start, heuristic, defaultEdges = getEdges).steps()
    }
}

fun main() = Day.runDay(Y2023D17::class)

//    Class creation: 14ms
//    Part 1: 635 (547ms)
//    Part 2: 734 (1139ms)
//    Total time: 1702ms

@Suppress("unused")
private val sampleInput = listOf(
    """2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
""", """111111111111
999999999991
999999999991
999999999991
999999999991
"""
)