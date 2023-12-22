package org.gristle.adventOfCode.y2023.d21

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.*

class Y2023D21(private val input: String) : Day {

    private val gardenWidth = input.indexOf('\n')
    
    private val gardenPath = let {
        val garden = input.toGrid()
        val start = garden.coordOf(garden.indexOf('S'))
        val q = ArrayDeque<Pair<Coord, Int>>()
            .apply { add(start to 0) }
        val visited = BooleanArray(garden.size)
            .apply { this[garden.indexOf(start)] = true }

        generateSequence { q.removeFirstOrNull() }
            .onEach { (pos, steps) ->
                val neighbors = listOf(pos.north(), pos.east(), pos.west(), pos.south())
                    .filter { neighbor ->
                        (garden.getOrNull(neighbor)?.let { it != '#'} == true
                                && !visited[garden.indexOf(neighbor)])
                            .also {
                                garden.indexOfOrNull(neighbor)?.let { visited[it] = true }
                            }
                    }.map { neighbor -> neighbor to steps + 1 }
                q.addAll(neighbors)
            }.map { (_, steps) -> steps }
            .toList()
    }

    override fun part1(): Int = gardenPath.count { it <= 64 && it.isEven() }

    // A detailed explanation for how this works is at:
    // https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21
    // Thanks, Viluna!
    override fun part2(): Long {
        val evenPath = gardenPath.filter { it.isEven() }
        val oddPath = gardenPath.filter { it.isOdd() }

        val evenCorners = evenPath.count { it > 65 }
        val oddCorners = oddPath.count { it > 65 && it.isOdd() }

        val n = (26501365 - gardenWidth / 2) / gardenWidth // number of extra squares in a given direction

        return (n + 1).pow(2) * oddPath.size + n.pow(2) * evenPath.size - (n + 1) * oddCorners + n * evenCorners
    }
}

fun main() = Day.runDay(Y2023D21::class)

//    Class creation: 55ms
//    Part 1: 3782 (2ms)
//    Part 2: 630661863455116 (6ms)
//    Total time: 64ms


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
""", """...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##......#.
.......#...
.##.#.####.
.##..##.##.
...........
""",
)