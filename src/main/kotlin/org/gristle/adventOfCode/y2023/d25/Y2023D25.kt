package org.gristle.adventOfCode.y2023.d25

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Graph

class Y2023D25(input: String) : Day {

    private val components = buildMap<String, MutableList<String>> {
        input.lines().map { line ->
            val split = line.split(": ", " ")
            for (component in split.drop(1)) {
                getOrPut(split[0]) { mutableListOf() }.add(component)
                getOrPut(component) { mutableListOf() }.add(split[0])
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
        
        val getEdges = { component: String ->
            components
                .getValue(component)
                .filter { connection -> setOf(component, connection) !in bridges }
        }
        
        val groupA = Graph.bfs(bridges[0].first(), defaultEdges = getEdges).size
        val groupB = Graph.bfs(bridges[0].last(), defaultEdges = getEdges).size
        return groupA * groupB
    } 

    override fun part2() = "Merry Xmas!"
}

fun main() = Day.runDay(Y2023D25::class)

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