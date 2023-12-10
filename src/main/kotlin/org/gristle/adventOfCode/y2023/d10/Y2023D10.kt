package org.gristle.adventOfCode.y2023.d10

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.*
import java.lang.IllegalStateException

class Y2023D10(input: String) : Day {

    private val pipes: Grid<Char> = input.toGrid()

    val edges: (Coord) -> List<Coord> = { pos ->
        validDirections.getValue(pipes[pos])
            .map { direction -> direction to pos.move(direction) }
            .filter { (direction, neighborPos) ->
                pipes.validCoord(neighborPos) &&
                        direction in receivingDirections.getValue(pipes[neighborPos])
            }.map { (_, neighborPos) -> neighborPos }
    }
    
    private val start = pipes.coordOfElement('S')
    
    private val loop: List<Graph.Vertex<Coord>> by lazy {
        Graph.bfs(start, defaultEdges = edges)
    } 

    override fun part1() = loop.maxOf { it.weight }.toInt()
    
    override fun part2(): Int {

        val startMimic: Char = let {
            val neighbors: Set<Nsew> = validDirections.getValue(pipes[start])
                .map { direction -> direction to start.move(direction) }
                .filter { (direction, neighborPos) ->
                    pipes.validCoord(neighborPos) &&
                            direction in receivingDirections.getValue(pipes[neighborPos])
                }.map { (direction, _) -> direction }
                .toSet()
            validDirections.entries.find { it.value == neighbors }
                ?.key
                ?: throw IllegalStateException("Cannot find 'S' connectors.")
        }

        val loopSet = loop.map { it.id }.toSet()

        val expandedPipe: Set<Coord> = loopSet.flatMap { pos ->
            val fitting: Char = pipes[pos].let { if (it == 'S') startMimic else it }
            val x = pos.x * 3
            val y = pos.y * 3
            fitting.expand().map { Coord(x + it % 3, y + it / 3) }
        }.toSet()

        val expandedEdges: (Coord) -> List<Coord> = { pos ->
            pos.getNeighbors()
                .filter { neighbor ->
                    neighbor.x in 0 until pipes.width * 3
                            && neighbor.y in 0 until pipes.height * 3 
                            && neighbor !in expandedPipe
                }
        } 
        
        val outsideOfLoop: Set<Coord> = Graph.bfs(Coord.ORIGIN, defaultEdges = expandedEdges)
            .map { it.id }
            .toSet()
        
        val pipePlusFlood = expandedPipe + outsideOfLoop

        val shortSequence = (0..9).asSequence()
        
        return pipes.coords().count { pos ->
            val x = pos.x * 3
            val y = pos.y * 3
            shortSequence.none { 
                Coord(x + it % 3, y + it / 3) in pipePlusFlood
            }
        }
    }
    
    companion object {
        private val validDirections: Map<Char, Set<Nsew>> = listOf(
            'S' to setOf(Nsew.NORTH, Nsew.EAST, Nsew.SOUTH, Nsew.WEST),
            '|' to setOf(Nsew.NORTH, Nsew.SOUTH),
            '-' to setOf(Nsew.EAST, Nsew.WEST),
            'L' to setOf(Nsew.NORTH, Nsew.EAST),
            'J' to setOf(Nsew.NORTH, Nsew.WEST),
            '7' to setOf(Nsew.SOUTH, Nsew.WEST),
            'F' to setOf(Nsew.SOUTH, Nsew.EAST),
            '.' to emptySet()
        ).toMap()

        private val receivingDirections: Map<Char, Set<Nsew>> =
            validDirections.entries.associate { (section, directions) ->
                section to directions.map { it.flip() }.toSet()
            }

        private fun Char.expand() = when (this) {
            '|' -> listOf(1, 4, 7)
            '-' -> listOf(3, 4, 5)
            'L' -> listOf(1, 4, 5)
            'J' -> listOf(1, 3, 4)
            '7' -> listOf(3, 4, 7)
            'F' -> listOf(4, 5, 7)
            else -> emptyList()
        }
    }
}

fun main() = Day.runDay(Y2023D10::class)

//    Class creation: 15ms
//    Part 1: 7086 (52ms)
//    Part 2: 317 (465ms)
//    Total time: 532ms

@Suppress("unused")
private val sampleInput = listOf(
    """.....
.S-7.
.|.|.
.L-J.
.....
""", """..F7.
.FJ|.
SJ.L7
|F--J
LJ...
""", """...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........
""", """..........
.S------7.
.|F----7|.
.||....||.
.||....||.
.|L-7F-J|.
.|..||..|.
.L--JL--J.
..........
""",""".F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...
""", """FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
""", 
)