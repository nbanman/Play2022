package org.gristle.adventOfCode.y2021.d12

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readInput
import java.util.*

object Y2021D12 {
    private val input = readInput("y2021/d12")
    
    // build a map of edges from each location
    private val edges = buildMap<String, MutableList<String>> { 
        input
            .map { it.split('-') }
            .forEach { (a, b) ->
                computeIfAbsent(a) { mutableListOf() }.add(b)
                if (a != "start" && b != "end") computeIfAbsent(b) { mutableListOf() }.add(a)
            }
    } as Map<String, List<String>>

    // Custom BFS that searches entire space, looking for number of possible paths rather than shortest path.
    // Uses custom state object to efficiently conform with part2 navigation rules.
    private fun findPaths(allowOneExtra: Boolean = false): Int {
        
        // Uses custom state object that tracks whether a small cave has been visited twice. Speeds up process
        // 3x over previous method of checking for duplicates in the visited list.
        class State(val visits: List<String>, val visitedTwice: Boolean = false)

        // bfs
        var count = 0
        val q: Deque<State> = ArrayDeque()
        q.add(State(listOf("start")))
        while (q.isNotEmpty()) {
            val v = q.poll()
            if (v.visits.last() == "end") {
                count++
            } else {
                val newPaths = edges
                    .getValue(v.visits.last())
                    .mapNotNull { edge ->
                        when {
                            edge[0].isUpperCase() || edge !in v.visits -> State(v.visits + edge, v.visitedTwice)
                            allowOneExtra && !v.visitedTwice -> State(v.visits + edge, true)
                            else -> null
                        }
                    }
                q.addAll(newPaths)
            }
        }
        return count
    }
    
    fun part1() = findPaths()
    fun part2() = findPaths(true)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D12.part1()} (${elapsedTime(time)}ms)") // 4104 (61ms old and new)
    time = System.nanoTime()
    println("Part 2: ${Y2021D12.part2()} (${elapsedTime(time)}ms)") // 119760 (417ms old, 130ms new)
}