package org.gristle.adventOfCode.y2022.d12

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.takeUntil
import org.gristle.adventOfCode.utilities.toGrid

class Y2022D12(input: String) : Day {

    private val area = input.toGrid()

    private fun Char.height() = when (this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    }.code

    private val startId = area.indexOf('E')

    private val getEdges = { pos: Int ->
        area
            .getNeighborsIndexedValue(pos)
            .filter { (_, c) -> c.height() >= area[pos].height() - 1 }
            .map { (index, _) -> index }
    }

    private val vertices = Graph
        .bfsSequence(
            startId = startId,
            defaultEdges = getEdges
        ).takeUntil { area[it.id] == 'S' }

    override fun part1() = vertices.toList().steps()
    override fun part2() = vertices.first { area[it.id] in "Sa" }.weight.toInt()

//    Alternate solution uses A* and is slightly faster, works for all known puzzle inputs but part 2 relies on quirk of
//    inputs that has all possible end points on the far left column of the grid. So the BFS solution above is more
//    general, though less fun.
//
//        fun solve(heuristic: (Coord) -> Double): Int {
//            val getEdges = { pos: Coord ->
//                area
//                    .getNeighborsIndexedValue(pos)
//                    .filter { (_, c) -> c.height() >= area[pos].height() - 1 }
//                    .map { (index, _) -> area.coordOf(index) }
//            }
//            return Graph
//                .aStar(
//                    area.coordOf(startId),
//                    heuristic = heuristic,
//                    defaultEdges = { pos -> getEdges(pos).toEdges() }
//                ).steps()
//        }
//
//        override fun part1(): Int {
//            val target = area.coordOf(area.indexOf('S'))
//            return solve(heuristic = { pos -> pos.manhattanDistance(target).toDouble() })
//        }
//
//        override fun part2() = solve(heuristic = { pos -> pos.x.toDouble() })

}

fun main() = Day.runDay(Y2022D12::class) // 361, 354

//    Class creation: 24ms
//    Part 1: 361 (19ms)
//    Part 2: 354 (6ms)
//    Total time: 50ms