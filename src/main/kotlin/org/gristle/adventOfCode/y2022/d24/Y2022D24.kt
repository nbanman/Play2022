package org.gristle.adventOfCode.y2022.d24

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.utilities.Graph.steps

class Y2022D24(input: String) {

    private val valley = input.toGrid()

    data class Blizzard(val initial: Int, val sign: Int, val valleySize: Int) {
        fun locationAt(n: Int) = (initial - 1 + (sign * n)).mod(valleySize) + 1
    }

    private val valleySize = Coord(valley.width - 2, valley.height - 2)

    private val blizzards = buildMap<Int, MutableList<Blizzard>> {
        valley.forEachIndexed { index, c ->
            val pos = Coord.fromIndex(index, valley.width)
            val (movement, eastWest) = when (c) {
                '^' -> -1 to false
                'v' -> 1 to false
                '<' -> -1 to true
                '>' -> 1 to true
                else -> null to true
            }
            if (movement != null) {
                if (eastWest) {
                    this.getOrPut(pos.y) { mutableListOf() }.add(Blizzard(pos.x, movement, valleySize.x))
                } else {
                    this.getOrPut(pos.x + valley.width) { mutableListOf() }.add(Blizzard(pos.y, movement, valleySize.y))
                }
            }
        }
    } as Map<Int, List<Blizzard>>


    data class State(val pos: Coord, val minute: Int)

    private val cross = listOf(
        Coord(0, -1), Coord(-1, 0), Coord(0, 0), Coord(1, 0), Coord(0, 1),
    )

    fun valleyString(pos: Coord, minute: Int, blizzards: Map<Int, List<Blizzard>>): String = buildString {
        val bList = blizzards.flatMap { (key, value) ->
            value.map { blizzard ->
                if (key >= valley.width) {
                    Triple(Coord(key - valley.width, blizzard.locationAt(minute)), blizzard, false)
                } else {
                    Triple(Coord(blizzard.locationAt(minute), key), blizzard, true)
                }
            }
        }
        val bMap = bList.associate { it.first to (it.second to it.third) }
        val blizzardCoords = bList.map { it.first }.eachCount()
        for (y in 0 until valley.height) {
            for (x in 0 until valley.width) {
                val c = when {
                    Coord(x, y) == pos -> 'E'
                    valley[x, y] == '#' -> '#'
                    else -> {
                        blizzardCoords[Coord(x, y)]
                            ?.let {
                                if (it > 1) it.digitToChar() else {
                                    val (bpos, eastWest) = bMap.getValue(Coord(x, y))
                                    if (eastWest) {
                                        if (bpos.sign == -1) '<' else '>'
                                    } else if (bpos.sign == -1) '^' else 'v'
                                }
                            }
                            ?: '.'
                    }
                }
                append(c)
            }
            append('\n')
        }
    }

    fun part1(): Int {

        val start = State(Coord.fromIndex(valley.indexOfFirst { it == '.' }, valley.width), 1)
        val end = Coord.fromIndex(valley.indexOfLast { it == '.' }, valley.width)
        val defaultEdges = { (pos, minute): State ->
            cross.map { it + pos }
                .filter { candidate ->
                    val predicate = valley.validCoord(candidate)
                            && valley[candidate] != '#'
                            && blizzards[candidate.y]?.find { it.locationAt(minute) == candidate.x } == null
                            && blizzards[candidate.x + valley.width]?.find { it.locationAt(minute) == candidate.y } == null
                    predicate
                }.map {
                    Graph.Edge(State(it, minute + 1), 1.0)
                }
        }
        val distances = Graph.aStar(
            startId = start,
            heuristic = { state -> state.pos.manhattanDistance(end).toDouble() },
            defaultEdges = defaultEdges
        )
        distances.forEach { currentVertex ->
            val action = currentVertex.parent
                ?.let { parentVertex ->
                    val narr = when (currentVertex.id.pos - parentVertex.id.pos) {
                        Coord.ORIGIN -> "wait"
                        Coord(0, -1) -> "move up"
                        Coord(0, 1) -> "move down"
                        Coord(1, 0) -> "move right"
                        else -> "move left"
                    }
                    "Minute ${currentVertex.weight.toInt()}, $narr:"
                } ?: "Initial state:"
            println(action)
            println(valleyString(currentVertex.id.pos, currentVertex.id.minute - 1, blizzards))
        }
        return distances.steps()
    }

    fun part2() = "To be implemented"
}

fun main() {
    val input = listOf(
        getInput(24, 2022),
        """#.######
#>>.<^<#
#.<..<<#
#>v.><>#
#<^v^^>#
######.#"""
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D24(input[1])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // (59) (277 solver)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // (877 solver)
    println("Total time: ${timer.elapsed()}ms")
}