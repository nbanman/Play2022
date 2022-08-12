package org.gristle.adventOfCode.y2021.d12

import org.gristle.adventOfCode.utilities.*
import java.util.*

object Y2021D12 {
    private val input = readInput("y2021/d12")

    class Graph {
        val edges = mutableMapOf<String, MutableList<String>>()

        override fun toString(): String {
            return "Graph(edges=$edges)"
        }
    }

    val graph = Graph()

    init {
        input.map { it.split('-') }.forEach { ls ->
            val a = ls.first()
            val b = ls.last()
            val edgesA = graph.edges.computeIfAbsent(a) { mutableListOf() }
            val edgesB = graph.edges.computeIfAbsent(b) { mutableListOf() }
            if (b != "start" && a != "end") edgesA.add(b)
            if (a != "start" && b != "end") edgesB.add(a)
        }
    }

    fun findPaths(allowOneExtra: Boolean = false): Int {
        fun List<String>.noDoubleVisits(): Boolean {
            return filter { it[0].isLowerCase() }
                .let { it.distinct().size == it.size }
        }

        // bfs
        var count = 0
        val q: Deque<List<String>> = LinkedList()
        q.add(listOf("start"))
        while (q.isNotEmpty()) {
            val v = q.poll()
            if (v.last() == "end") {
                count ++
            }
            val n = graph.edges[v.last()]!!.filter { edge ->
                edge[0].isUpperCase()
                        || v.find { it == edge } == null
                        || (allowOneExtra && v.noDoubleVisits())

            }
            val newPaths = n.map { v + it }
            q.addAll(newPaths)
        }
        return count
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D12.findPaths()} (${elapsedTime(time)}ms)") // 4104
    time = System.nanoTime()
    println("Part 2: ${Y2021D12.findPaths(true)} (${elapsedTime(time)}ms)") // 119760
}