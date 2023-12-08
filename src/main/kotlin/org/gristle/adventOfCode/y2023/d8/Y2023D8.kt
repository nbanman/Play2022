package org.gristle.adventOfCode.y2023.d8

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.lcm

class Y2023D8(input: String) : Day {

    private val directions: Sequence<Nsew>
    private val network: Map<String, Pair<String, String>>
    
    init {
        val (dirStr, netStr) = input.blankSplit()
        directions = sequence { 
            val dirList = dirStr.map(Nsew::of)
            while (true) {
                for (dir in dirList) yield(dir)
            }
        }
        network = buildMap {
            Regex("""\w{3}""")
                .findAll(netStr)
                .map(MatchResult::value)
                .chunked(3) 
                .forEach { (node, left, right) -> put(node, left to right) }
        }
    }
    
    private fun traverseSequence(startNode: String): Sequence<String> = sequence { 
        var node = startNode
        yield(node)
        for (dir in directions) {
            node = network.getValue(node).let { (left, right) -> if (dir == Nsew.WEST) left else right }
            yield(node)
        }
    }

    private inline fun traverse(startNode: String, endCondition: (String) -> Boolean): Int = 
        traverseSequence(startNode).indexOfFirst(endCondition)
    
    override fun part1() = traverse("AAA") { it == "ZZZ" }

    override fun part2(): Long {
        val startNodes = network.keys
            .filter { it.last() == 'A' }
            .map { node -> traverse(node) { it.last() == 'Z' }.toLong() }
        
        return lcm(startNodes)
    }
}

fun main() = Day.runDay(Y2023D8::class)

//    Class creation: 17ms
//    Part 1: 19241 (14ms)
//    Part 2: 9606140307013 (32ms)
//    Total time: 64ms

@Suppress("unused")
private val sampleInput = listOf(
    """RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)
""", """LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
""",
)

@Suppress("unused")
private val sampleInput2 = listOf(
    """LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
""" to "6"
)