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

    private fun solve(minimumStraight: Int, wobblyRules: (State, Nsew) -> Boolean): Int {
        val start = State(Coord.ORIGIN, Nsew.EAST, 0)
        val end = city.coordOf(city.lastIndex)
        val heuristic: (State) -> Double = { (pos) -> pos.manhattanDistance(end).toDouble() }
        val endCondition: (State) -> Boolean = { state -> state.straights >= minimumStraight && state.pos.manhattanDistance(end) == 0 }
        val getEdges: (State) -> List<Graph.Edge<State>> = { state ->
            listOf(state.dir, state.dir.left(), state.dir.right())
                .filter { wobblyRules(state, it) }
                .map { dir -> state.pos.move(dir) to dir }
                .filter { (pos) -> city.validCoord(pos) }
                .map { (pos, dir) ->
                    val straights = if (dir == state.dir) state.straights + 1 else 1
                    val newState = State(pos, dir, straights)
                    Graph.Edge(newState, city[newState.pos].toDouble())
                }
        }
        val path = Graph.aStar(start, heuristic, endCondition, defaultEdges = getEdges)
        return path.steps()
    }

    override fun part1() = solve(0) { state, newDir -> (newDir != state.dir || state.straights < 3) }
    override fun part2(): Int = solve(4) { state, newDir -> 
        val result = when (state.straights) {
            in 1..3 -> state.dir == newDir
            10 -> state.dir != newDir
            else -> true
        }
        result
    }

}
//fun main() = Day.runDay(Y2023D17::class, sampleInput[0])
fun main() = Day.runDay(Y2023D17::class)

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