package org.gristle.adventOfCode.y2023.d25

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Graph

class Y2023D25(input: String) : Day {

    private val components: Map<String, List<String>> = buildMap<String, MutableList<String>> {
        input.lines().map { line ->
            val componentNames = line.split(": ", " ")
            for (component in componentNames.drop(1)) {
                getOrPut(componentNames[0]) { mutableListOf() }.add(component)
                getOrPut(component) { mutableListOf() }.add(componentNames[0])
            }
        }
    }

    override fun part1(): Int {
        // find the "bridge" components by doing a flood fill from each component. The component pairs with the most
        // connections are the least avoidable ones because you have to use them to cross over.
        val bridges = components.keys
            .flatMap { component ->
                Graph.bfsSequence(component) { components.getValue(it) }
                    .mapNotNull { it.parent?.let { parent -> setOf(it.id, parent.id) } }
            }.groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(3)
            .map { it.key }
        
        // used to speed up the filtering in the getEdges lambda below
        val bridgeEnds = bridges.flatten()
        
        // We get the size of each island by running a flood fill on either side of a bridge, modifying the edge
        // finder to filter out the bridge connections.
        val getEdges = { component: String ->
            if (component in bridgeEnds) {
                components
                    .getValue(component)
                    .filter { connection -> setOf(component, connection) !in bridges }
            } else {
                components.getValue(component)
            }
        }
        return bridges[0].fold(1) { acc, side -> acc * Graph.bfs(side, defaultEdges = getEdges).size }
    } 

    override fun part2() = "Merry Xmas!"
}

fun main() = Day.runDay(Y2023D25::class)

//    Class creation: 23ms
//    Part 1: 569904 (2017ms)
//    Total time: 2041ms

@Suppress("unused")
private val sampleInput = listOf(
    """jqt: rhn xhk nvd
rsh: frs pzl lsr
xhk: hfx
cmg: qnr nvd lhk bvb
rhn: xhk bvb hfx
bvb: xhk hfx
pzl: lsr hfx nvd
qnr: nvd
ntq: jqt hfx bvb xhk
nvd: lhk
lsr: lhk
rzs: qnr cmg lsr rsh
frs: qnr lhk lsr
""",
)