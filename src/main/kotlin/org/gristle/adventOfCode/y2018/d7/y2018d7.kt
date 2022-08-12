package org.gristle.adventOfCode.y2018.d7

import org.gristle.adventOfCode.utilities.*

object Y2018D7 {
    private val input = readRawInput("y2018/d7")

    val pattern = """Step ([A-Z]) must be finished before step ([A-Z]) can begin.""".toRegex()

    val instructions = input.groupValues(pattern) { it[0] }

    private val steps = mutableMapOf<Char, MutableList<Char>>().apply {
        instructions.forEach { instruction ->
            computeIfAbsent(instruction[0]) { mutableListOf<Char>() }.add(instruction[1])
        }
    }

    private val reverseSteps = mutableMapOf<Char, MutableList<Char>>().apply {
        instructions.forEach { instruction ->
            computeIfAbsent(instruction[1]) { mutableListOf<Char>() }.add(instruction[0])
        }
    }

    private val start = (instructions.map { it[0] }.toSet() - instructions.map { it[1] }.toSet())

    fun part1(): String {
        val sb = StringBuilder()
        val potentials = start.toMutableList()
        while (potentials.isNotEmpty()) {
            val c = potentials.filter { potential ->
                val dependencies = reverseSteps[potential]
                dependencies == null || dependencies.all { it in sb }
            }.first()
            potentials.remove(c)
            sb.append(c)
            val children = (steps[c] ?: mutableListOf()).filter { it !in sb && it !in potentials }
            potentials.addAll(children)
            potentials.sort()
        }
        return sb.toString()
    }

    fun part2(workers: Int, offset: Int): Int {
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

        val workerPool = List<Worker>(workers) { Worker('.', 0) }

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
                    worker.whenReady = sec + offset + c.toInt() - 64
                    potentials.remove(c)
                }
            }.first { done.size == doneSize }
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2018D7.part1()} (${elapsedTime(time)}ms)") // ABGKCMVWYDEHFOPQUILSTNZRJX
    time = System.nanoTime()
    println("Part 2: ${Y2018D7.part2(5, 60)} (${elapsedTime(time)}ms)") // 898
}