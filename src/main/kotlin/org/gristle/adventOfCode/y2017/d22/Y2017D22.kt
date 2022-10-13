package org.gristle.adventOfCode.y2017.d22

import org.gristle.adventOfCode.utilities.*

class Y2017D22(input: String) {
    enum class NodeState {
        CLEAN {
            override fun advance() = WEAKENED
        },
        WEAKENED {
            override fun advance() = INFECTED
        },
        INFECTED {
            override fun advance() = FLAGGED
        },
        FLAGGED {
            override fun advance() = CLEAN
        };

        abstract fun advance(): NodeState
    }

    class Virus(
        val pos: Coord,
        val dir: Nsew,
        val nodes: MutableMap<Coord, NodeState>,
        private val burst: Virus.() -> Virus,
        val infections: Int
    ) {
        fun move(infected: Boolean, turn: Nsew.() -> Nsew): Virus {
            val newDir = dir.turn()
            return Virus(pos.move(newDir), newDir, nodes, burst, infections + if (infected) 1 else 0)
        }
    }

    private val initialPos: Coord

    private val initialNodes: Map<Coord, NodeState>

    init {
        input.toGrid().let { grid ->
            initialPos = Coord(grid.width / 2, grid.height / 2)
            initialNodes = buildMap {
                grid.forEachIndexed { index, c -> if (c == '#') put(grid.coordOf(index), NodeState.INFECTED) }
            }
        }
    }

    fun solve(bursts: Int, burst: Virus.() -> Virus): Int {
        val virus = Virus(
            initialPos,
            Nsew.NORTH,
            initialNodes.toMutableMap().withDefault { NodeState.CLEAN },
            burst,
            0
        )
        return (1..bursts).fold(virus) { acc, _ -> acc.burst() }.infections
    }

    fun part1(): Int {
        val burst: Virus.() -> Virus = {
            val currentNode = nodes.getValue(pos)
            if (currentNode == NodeState.CLEAN) {
                nodes[pos] = NodeState.INFECTED
                move(true, Nsew::left)
            } else {
                nodes[pos] = NodeState.CLEAN
                move(false, Nsew::right)
            }
        }
        return solve(10_000, burst)
    }
    
    fun part2(): Int {
        val burst: Virus.() -> Virus = {
            val currentNode = nodes.getValue(pos)
            nodes[pos] = currentNode.advance()
            when (currentNode) {
                NodeState.CLEAN -> move(false, Nsew::left)
                NodeState.WEAKENED -> move(true) { this }
                NodeState.INFECTED -> move(false, Nsew::right)
                NodeState.FLAGGED -> move(false, Nsew::opposite)
            }
        }
        return solve(10_000_000, burst)
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D22(readRawInput("y2017/d22"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 5348 (25ms)
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2512225 (2033ms)
}