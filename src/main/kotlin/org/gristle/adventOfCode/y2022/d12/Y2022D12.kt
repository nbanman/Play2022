package org.gristle.adventOfCode.y2022.d12

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.utilities.Graph.steps

class Y2022D12(input: String) {

    private val area = input.toGrid()

    private fun Char.height() = when (this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    }.code

    private val startId = area.coordOf(area.indexOf('E'))

    private val getEdges = { pos: Coord ->
        area
            .getNeighborsIndexedValue(pos)
            .filter { (_, c) -> c.height() >= area[pos].height() - 1 }
            .map { (index, _) -> area.coordOf(index) }
    }

    private val vertices = Graph.bfs(
        startId = startId,
        endCondition = { pos -> area[pos] == 'S' },
        defaultEdges = getEdges
    )

    fun part1() = vertices.steps()
    fun part2() = vertices.firstOrNull { area[it.id] == 'a' }?.weight?.toInt() ?: vertices.steps()

//    Alternate solution uses A* and is slightly faster, works for all known puzzle inputs but part 2 relies on quirk of
//    inputs that has all possible end points on the far left column of the grid. So the BFS solution above is more
//    general, though less fun.

//        fun solve(heuristic: (Coord) -> Double): Int {
//            val getEdges = { pos: Coord ->
//                area
//                    .getNeighborsIndexedValue(pos)
//                    .filter { (_, c) -> c.height() >= area[pos].height() - 1 }
//                    .map { (index, _) -> area.coordOf(index) }
//            }
//            return Graph
//                .aStar(
//                    startId,
//                    heuristic = heuristic,
//                    defaultEdges = { pos -> getEdges(pos).map { Graph.Edge(it, 1.0) } }
//                ).steps()
//        }
//    
//        fun part1(): Int {
//            val target = area.coordOf(area.indexOf('S'))
//            return solve(heuristic = { pos -> pos.manhattanDistance(target).toDouble() })
//        }
//    
//        fun part2() = solve(heuristic = { pos -> pos.x.toDouble() })

}

fun main() {
    val input = getInput(12, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D12(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 361
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 354
    println("Total time: ${timer.elapsed()}ms")
}