package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.Edge
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.toGrid
import kotlin.math.min

fun main() {
    val (input1, input2, input3) = ecInputs(24, 13)
    val timer = Stopwatch(true)
    println("1. ${solve(input1)}: ${timer.lap()}ms")
    println("2. ${solve(input2)}: ${timer.lap()}ms")
    println("3. ${solve(input3)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private data class State(val floor: Int, val pos: Int)

private fun solve(input: String): Int {
    val chamber = input.toGrid()
    val start = chamber.indexOf('E')
    val getNeighbors = { state: State ->
        chamber
            .getNeighborsIndexedValue(state.pos)
            .filter { (_, c) -> c.isLetterOrDigit() }
            .map { (neighborPos, c) ->
                val neighborFloor = c.digitToIntOrNull() ?: 0
                val timeCost = 1 + min(
                    (neighborFloor - state.floor).mod(10),
                    (state.floor + 10 - neighborFloor).mod(10)
                )
                Edge(State(neighborFloor, neighborPos), timeCost.toDouble())
            }
    }
    return Graph.dijkstraSequence(startId = State(0, start), defaultEdges = getNeighbors)
        .first { vertex -> chamber[vertex.id.pos] == 'S' }
        .steps()
}