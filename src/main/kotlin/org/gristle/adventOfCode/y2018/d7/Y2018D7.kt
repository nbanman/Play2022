package org.gristle.adventOfCode.y2018.d7

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2018D7(input: String) {

    val pattern = """Step ([A-Z]) must be finished before step ([A-Z]) can begin.""".toRegex()

    val instructions = input.groupValues(pattern) { it[0] }

    private val steps = buildMap<Char, MutableList<Char>> {
        instructions.forEach { instruction ->
            computeIfAbsent(instruction[0]) { mutableListOf() }.add(instruction[1])
        }
    } as Map<Char, List<Char>>

    private val reverseSteps = buildMap<Char, MutableList<Char>> {
        instructions.forEach { instruction ->
            computeIfAbsent(instruction[1]) { mutableListOf() }.add(instruction[0])
        }
    } as Map<Char, List<Char>>

    private val start = (instructions.map { it[0] }.toSet() - instructions.map { it[1] }.toSet())

    fun part1(): String {
        val sb = StringBuilder()
        val potentials = start.toMutableList()
        while (potentials.isNotEmpty()) {
            val c = potentials.first { potential ->
                val dependencies = reverseSteps[potential]
                dependencies == null || dependencies.all { it in sb }
            }
            potentials.remove(c)
            sb.append(c)
            val children = (steps[c] ?: mutableListOf()).filter { it !in sb && it !in potentials }
            potentials.addAll(children)
            potentials.sort()
        }
        return sb.toString()
    }

    fun part2(workers: Int = 5, offset: Int = 60): Int {
        // done tracks the letters that have been delivered
        val done = mutableSetOf<Char>()
        // doneSize used to terminate the sequence. When "done" has all the letters, it will stop.
        val doneSize = (steps.keys + reverseSteps.keys).size
        // potentials is the list of letters that are POTENTIALLY available because at least one
        // dependency has been completed. However, they may not be available because other dependencies
        // may not yet have been completed.
        val potentials = start.toMutableList()

        // Worker tracks what a worker is working on and when they'll finish. Idle workers work on '.'
        data class Worker(var workingOn: Char, var whenReady: Int)

        val workerPool = List(workers) { Worker('.', 0) }

        // This sequence starts at second 0 and keeps adding one second.
        // Each second, it harvests completed letters from workers, adding them to the "done" set.
        // It then assigns available letters to idle workers.
        // It terminates when the "done" set contains all the letters, returning the # of seconds.
        return generateSequence (0) { it + 1 }
            .onEach { sec ->
                // finished workers deliver product and become ready to take on new letter
                workerPool.forEach { worker ->
                    if (worker.whenReady == sec && worker.workingOn.isUpperCase()) {
                        done.add(worker.workingOn)
                        val children = (steps[worker.workingOn]
                            ?: mutableListOf()).filter { it !in done && it !in potentials }
                        potentials.addAll(children)
                        potentials.sort()
                        worker.workingOn = '.'
                    }
                }
                // idle workers take on new projects
                // find available letters (completed step shows available AND all dependencies completed)
                val vettedPotentials = potentials.filter { potential ->
                    val dependencies = reverseSteps[potential]
                    dependencies == null || dependencies.all { it in done }
                }
                // match those letters to idle workers, and start the workers working on them. Remove from
                // potentials pool
                workerPool.filter{ it.workingOn == '.' }.zip(vettedPotentials).forEach { (worker, c) ->
                    worker.workingOn = c
                    worker.whenReady = sec + offset + c.code - 64
                    potentials.remove(c)
                }
            }.first { done.size == doneSize }
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D7(readRawInput("y2018/d7"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // ABGKCMVWYDEHFOPQUILSTNZRJX
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 898
}