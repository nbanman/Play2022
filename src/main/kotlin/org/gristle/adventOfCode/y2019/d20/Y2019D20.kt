package org.gristle.adventOfCode.y2019.d20

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.Graph.steps
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.toGrid

class Y2019D20(input: String) : Day {

    class E1920(val node: N1920, val weight: Int) {
        override fun toString(): String {
            return "E1920(index=${node.locator}, name=${node.name}, weight=$weight)"
        }
    }

    data class N1920(private val rep: Char, val locator: Int, val grid: Grid<Char>) {

        var name = if (!rep.isUpperCase()) {
            rep.toString()
        } else {
            Nsew.entries
                .mapNotNull { dir ->
                    val coord = grid.coordOf(locator)
                    val forwardCoord = dir.forward(coord)
                    if (grid.validCoord(forwardCoord) && grid[forwardCoord] == '.') {
                        val letters = if (dir == Nsew.SOUTH || dir == Nsew.EAST) {
                            "${grid[dir.right().right().forward(coord)]}$rep"
                        } else {
                            "$rep${grid[dir.right().right().forward(coord)]}"
                        }
                        if (letters in "AAZZ") {
                            letters
                        } else {
                            val portalType = if (grid.validCoord(dir.right().right().forward(coord, 2))) "in " else "out"
                            letters + portalType
                        }
                    } else {
                        null
                    }
                }.let { if (it.isEmpty()) null else it.first() } ?: " "
        }

        var safeToDelete = name in " #AAZZ"

        private fun getOtherSide(nodes: Grid<N1920>): N1920 {
            return if (name.length != 5) {
                this
            } else {
                nodes
                    .find { name.dropLast(3) in it.name && locator != it.locator } // ?.let { otherPortal -> nodes.getNeighbors(otherPortal.locator).find { it.name == "." } }
                    ?: this
            }
        }

        val edges = mutableListOf<E1920>()

        fun getEdges(nodes: Grid<N1920>) {
            if (!safeToDelete) {
                if (name.length == 5) {
                    grid.getNeighborIndices(locator)
                        .filter {
                            grid[it] == '.'
                        }
                        .map { neighborIndex ->
                            E1920(nodes[neighborIndex], 0)
                        }
                        .apply {
                            edges.addAll(this)
                            edges.add(E1920(getOtherSide(nodes), 0))
                        }
                } else {
                    grid.getNeighborIndices(locator)
                        .filter {
                            if (nodes[it].name == "AA") name = "start"
                            if (nodes[it].name == "ZZ") name = "end"
                            grid[it] != '#' && nodes[it].name != "AA" && nodes[it].name != "ZZ"
                        }
                        .map { neighborIndex ->
                            val neighborNode =
                                nodes[neighborIndex]//.let { if (it.name.length == 5) it.getOtherSide(nodes) else it }
                            E1920(neighborNode, 1)
                        }
                        .apply { edges.addAll(this) }
                }
            }
        }

        private fun replaceEdge(index: Int, edge: E1920) {
            val edgeIndex = edges.indexOfFirst { it.node.locator == index }
            val oldWeight = edges[edgeIndex].weight
            val newEdge = E1920(edge.node, edge.weight + oldWeight)
            edges[edgeIndex] = newEdge
        }

        private fun deleteEdge(index: Int) {
            val edgeIndex = edges.indexOfFirst { it.node.locator == index }
            edges.removeAt(edgeIndex)
            safeDelete()
        }

        fun safeDelete(): Boolean {
            if (safeToDelete) return true
            if (name == ".") {
                when (edges.size) {
                    1 -> {
                        edges[0].node.deleteEdge(locator)
                        safeToDelete = true
                    }
                    2 -> {
                        edges[0].node.replaceEdge(locator, edges[1])
                        edges[1].node.replaceEdge(locator, edges[0])
                        safeToDelete = true
                    }
                }
            }
            return false
        }

        override fun toString(): String {
            return "N1920(\"$name\", locator=$locator, coord=${grid.coordOf(locator)}, edges=${edges.size}"
        }

    }

    private val maze = input.toGrid()
    private val nodes = maze.mapIndexed { index, c -> N1920(c, index, maze) }.toGrid(maze.width).apply {
        forEach { it.getEdges(this) }
        forEach(N1920::safeDelete)
    }
    private val start = nodes.find { it.name == "start" }!!
    private val end = nodes.find { it.name == "end" }!!

    override fun part1(): Int {
        val p1Nodes = nodes
            .filter { !it.safeToDelete }
            .map {
                val edges = it.edges.map { edge ->
                    Graph.Edge(edge.node.locator, edge.weight.toDouble())
                }
                it.locator to edges
            }.let {
                mutableMapOf(*it.toTypedArray())
            }

        val distance = Graph.dijkstra(start.locator, { u -> u == end.locator }, p1Nodes)
        return distance.steps()
    }

    override fun part2(): Int {
        data class DId1920(val index: Int, val name: String, val level: Int = 0) {
            override fun toString(): String {
                return "DId1920(index=$index, coord=${nodes.coordOf(index)}, name='$name', level=$level)"
            }
        }

        val distance = Graph.dijkstra(
            DId1920(start.locator, start.name),
            { u -> u == DId1920(end.locator, end.name) }
        ) { u ->
            nodes[u.index].edges.mapNotNull { neighbor ->
                when {
                    neighbor.node.name.takeLast(3) == "out" && u.level == 0 -> null
                    neighbor.node.name.takeLast(3) == "in " && nodes[u.index].name.dropLast(3) != neighbor.node.name.dropLast(3) -> {
                        Graph.Edge(
                            DId1920(neighbor.node.locator, neighbor.node.name, u.level + 1),
                            neighbor.weight.toDouble()
                        )
                    }
                    nodes[u.index].name.takeLast(3) == "in " && neighbor.node.name.dropLast(3) != nodes[u.index].name.dropLast(3) -> {
                        Graph.Edge(
                            DId1920(neighbor.node.locator, neighbor.node.name, u.level - 1),
                            neighbor.weight.toDouble()
                        )
                    }
                    else -> {
                        Graph.Edge(
                            DId1920(neighbor.node.locator, neighbor.node.name, u.level),
                            neighbor.weight.toDouble()
                        )
                    }
                }
            }
        }
        return distance.steps()
    }
}

fun main() = Day.runDay(Y2019D20::class)

//    Class creation: 87ms
//    Part 1: 528 (4ms)
//    Part 2: 6214 (52ms)
//    Total time: 144ms