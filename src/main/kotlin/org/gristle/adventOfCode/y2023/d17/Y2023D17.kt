package org.gristle.adventOfCode.y2023.d17

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.toGrid

class Y2023D17(input: String) : Day {

    private val city = input.toGrid { it.digitToInt() }

    private data class State(val pos: Coord, val dir: Nsew, val straights: Int)
    
    private val start = State(Coord.ORIGIN, Nsew.EAST, 0)
    private val end = city.coordOf(city.lastIndex)
    private val heuristic = { state: State -> state.pos.manhattanDistance(end).toDouble() }

    override fun part1(): Int {
        fun turnOrNull(state: State, turnDir: Nsew): Graph.Edge<State>? {
            val pos = state.pos.move(turnDir)
            val weight = city.getOrNull(pos)
            return if (weight != null) {
                val newState = State(pos, turnDir, 1)
                Graph.Edge(newState, weight.toDouble())
            } else {
                null
            }
        }
        
        val getEdges = { state: State -> 
            buildList {
                // continue straight
                if (state.straights < 3) {
                    val pos = state.pos.move(state.dir)
                    val weight = city.getOrNull(pos)
                    if (weight != null) {
                        val newState = State(pos, state.dir, state.straights + 1)
                        add(Graph.Edge(newState, weight.toDouble()))
                    }
                }
                // go left
                turnOrNull(state, state.dir.left())?.let { add(it) }

                // go right
                turnOrNull(state, state.dir.right())?.let { add(it) }
            }
        }
        
        val path = Graph.aStar(start, heuristic, defaultEdges = getEdges)
        return path.steps()
    }
    
    override fun part2(): Int {
        fun move(pos: Coord, dir: Nsew, distance: Int): Pair<Coord, Double>? =
            (1..distance).fold(pos to 0.0) { (currentPos, weight), _ ->
                val nextPos = currentPos.move(dir)
                if (!city.validCoord(nextPos)) return@move null
                val newWeight = weight + city[nextPos]
                nextPos to newWeight
            }

        val getEdges = { state: State ->
            buildList { 
                // continue straight
                val distance = when (state.straights) {
                    0 -> 4
                    in 4..9 -> 1
                    else -> 0
                }
                if (distance != 0) {
                    move(state.pos, state.dir, distance)?.let { (pos, weight) ->
                        val newState = State(pos, state.dir, state.straights + distance)
                        add(Graph.Edge(newState, weight))
                    }
                }
                
                // go left
                val leftDir = state.dir.left()
                move(state.pos, leftDir, 4)?.let { (pos, weight) ->
                    val newState = State(pos, leftDir, 4)
                    add(Graph.Edge(newState, weight))
                }

                // go right
                val rightDir = state.dir.right()
                move(state.pos, rightDir, 4)?.let { (pos, weight) ->
                    val newState = State(pos, rightDir, 4)
                    add(Graph.Edge(newState, weight))
                }
            }
        }
        val path = Graph.aStar(start, heuristic, defaultEdges = getEdges)
        return path.steps()
    }

}
//fun main() = Day.runDay(Y2023D17::class, sampleInput[1])
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