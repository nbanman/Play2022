package org.gristle.adventOfCode.y2023.d21

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.isEven
import org.gristle.adventOfCode.utilities.toGrid

class Y2023D21(input: String) : Day {

    private val garden = input.toGrid()
    private val start = garden.coordOf(garden.indexOf('S'))

    fun Grid<Char>.rep(plots: Iterable<Pair<Coord, Int>>): String {
        
        return buildString {
            val p = plots.map { it.first }.toSet()
            append('\n')
            for (row in yIndices) {
                for (col in xIndices) {
                    if (Coord(col, row) in p) {
                        append('O')
                    } else {
                        append(get(col, row))
                    }
                }
                append('\n')
            }
        }
    }
    
    fun IntArray.rep(): String = buildString { 
        for (y in garden.yIndices) {
            for (x in garden.xIndices) {
                if (this@rep[y * garden.width + x] == Int.MAX_VALUE) append('#') else append('.')
            }
            append('\n')
        }
    }

    override fun part1(): Int {
        val q = ArrayDeque<Pair<Coord, Int>>()
            .apply { add(start to 0) }
        val visited = BooleanArray(garden.size)
            .apply { this[garden.indexOf(start)] = true }

        val result = generateSequence { q.removeFirstOrNull() }
            .onEach { (pos, steps) ->
                if (steps < 64) {
                    val neighbors = listOf(pos.north(), pos.east(), pos.west(), pos.south())
                        .filter { neighbor ->
                            (garden.getOrNull(neighbor)?.let { it != '#'} == true
                                    && !visited[garden.indexOf(neighbor)])
                                .also {
                                    garden.indexOfOrNull(neighbor)?.let { visited[it] = true }
                                }
                        }.map { neighbor -> neighbor to steps + 1 }
                    q.addAll(neighbors)
                }
            }.toList()

        return result.filter{ (_, steps) -> steps.isEven() }.size
    }
    
    fun getEdges(pos: Coord): IntArray {
        val edges = IntArray(garden.size) { -1 }
            .apply { this[garden.indexOf(pos)] = 0 }
        val q = ArrayDeque<Pair<Coord, Int>>()
            .apply { add(pos to 0) }

        generateSequence { q.removeFirstOrNull() }
            .forEach { (current, steps) ->
                val neighbors = listOf(current.north(), current.east(), current.west(), current.south())
                    .filter { neighbor ->
                        (garden.getOrNull(neighbor)?.let { it != '#'} == true 
                                && edges[garden.indexOf(neighbor)] == -1)
                    }.map { neighbor -> neighbor to steps + 1 }
                    .onEach { (neighbor, steps) -> edges[garden.indexOf(neighbor)] = steps }

                q.addAll(neighbors)
            }
        return edges
    }
    
    override fun part2(): Long {
        val elfSteps = 26_501_365
        // start to any corner is 130
        // max steps to anywhere in incoming square is 195 because you can start from either corner
        // have to deal with the "fill" squares off the main cross at the edges. those can start from anywhere along
        // the edge?
        val cornerMaps: Map<Coord, IntArray> = 
            listOf(
                Coord.ORIGIN,
                Coord(garden.width - 1, 0),
                Coord(0, garden.height - 1),
                Coord(garden.width - 1, garden.height - 1)
            ).associateWith { getEdges(it) }
        val startToCorners: Map<Coord, Int> = cornerMaps
            .entries.associate { (corner, distances) -> corner to distances[garden.indexOf(start)] }
        val maxSteps = cornerMaps.map { (_, dists) -> dists.filter{ it != -1 }.max() }.max()
        val dimensions = (elfSteps - maxSteps) / garden.width
        val remaining = elfSteps % garden.width
        
        return -1
    }
}

//fun main() = Day.runDay(Y2023D21::class, sampleInput)
fun main() = Day.runDay(Y2023D21::class)

@Suppress("unused")
private val sampleInput = listOf(
    """...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
""",
)