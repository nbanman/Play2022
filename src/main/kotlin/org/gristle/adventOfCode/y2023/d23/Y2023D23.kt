package org.gristle.adventOfCode.y2023.d23

import org.gristle.adventOfCode.Day

class Y2023D23(private val trails: String) : Day {

    private val width = trails.indexOf('\n')
    private val start = trails.indexOf('.')
    private val end = trails.lastIndexOf('.')
    
    private fun findLongestTrail(edgeMap: Map<Int, List<Pair<Int, Int>>> = emptyMap()): Int {
        val visited = mutableSetOf<Int>()
        val possibilities = mutableListOf<Int>()
        val longestTrail = DeepRecursiveFunction<Pair<Int, Int>, Int> { (pos, weight) ->
            if (pos == end) {
                possibilities.add(weight)
                weight
            } else {
                visited.add(pos)
                val c = trails[pos]
                val neighbors = (edgeMap[pos] 
                    ?:let {
                        when (c) {
                            '^' -> listOf(pos - (width + 1) to 1)
                            '>' -> listOf(pos + 1 to 1)
                            'v' -> listOf(pos + (width + 1) to 1)
                            '<' -> listOf(pos - 1 to 1)
                            else -> listOf(pos - (width + 1), pos + 1, pos - 1, pos + (width + 1)).map { it to 1 }
                        }.filter { (neighbor, _) ->
                            trails.getOrNull(neighbor)?.let { it !in "#\n" } == true 
                        }
                    }).filter { (neighbor, _) -> neighbor !in visited }
                    .map { (neighbor, newWeight) -> neighbor to weight + newWeight }
                val maxDistance = neighbors
                    .maxOfOrNull { state ->
                        callRecursive(state)
                    } ?: 0
                visited.remove(pos)
                maxDistance
            }
        }
        val result = longestTrail(start to 0)
        return result
    }
    
    override fun part1() = findLongestTrail()

    private fun connectVertex(pos: Int, vertices: Set<Int>): List<Pair<Int, Int>> {
        val visited = mutableSetOf<Int>()
        val q = mutableListOf(pos to 0)
        return generateSequence { q.removeLastOrNull() }
            .onEach { (current, dist) ->
                if (current !in vertices || current == pos) {
                    val neighbors = listOf(current - (width + 1), current + 1, current - 1, current + (width + 1))
                        .filter { neighbor ->
                            trails.getOrNull(neighbor)?.let { it !in "#\n"} == true && neighbor !in visited
                        }
                    neighbors.forEach { visited.add(it) }
                    q.addAll(neighbors.map { it to dist + 1 })
                }
            }.filter { (current, _) -> current != pos && current in vertices }
            .toList()
    }
    
    override fun part2(): Int {
        val edgeMap: Map<Int, List<Pair<Int, Int>>> = buildMap<Int, MutableList<Pair<Int, Int>>> {
            // get weighted vertices
            val vertices = buildSet {
                val branchPoints = trails
                    .withIndex()
                    .filter { (pos, c) ->
                        c !in "#\n" && 3 <= listOf(pos - (width + 1), pos + 1, pos - 1, pos + (width + 1))
                            .count { neighbor -> trails.getOrNull(neighbor)?.let { it != '#' } == true }
                    }.map { (pos, _) -> pos }
                add(start)
                addAll(branchPoints)
                add(end)
            }
            
            vertices.forEach { pos ->
                connectVertex(pos, vertices).forEach { (neighbor, dist) ->
                    getOrPut(pos) { mutableListOf() }.add(neighbor to dist)
                }
            }
        }
        
        val result = findLongestTrail(edgeMap)
        return result
    } 
}

fun main() = Day.runDay(Y2023D23::class)

@Suppress("unused")
private val sampleInput = listOf(
    """#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
""",
)