package org.gristle.adventOfCode.y2017.d12

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

object Y2017D12 {
    private val input = readRawInput("y2017/d12")

    fun allLinks(links: Map<Int, List<Int>>, seed: Int): List<Int> {

        tailrec fun aL(found: Set<Int>, evaluate: Set<Int>): Set<Int> {
            return if(evaluate.isEmpty()) {
                found
            } else {
                val newFound = found + evaluate
                val newRegisters = evaluate.fold(listOf<Int>()) { acc, i ->
                    acc + links[i]!!.filter { it !in newFound }
                }.toSet()
                aL(found + evaluate, newRegisters)
            }
        }
        return aL(emptySet(), setOf(seed)).toList()
    }

    private const val pattern = """(\d+) <-> (\d+(?:, \d+)*)"""

    private val links = input
        .groupValues(pattern)
        .let { gvs ->
            val massaged = gvs.map { gv ->
                val values = gv[1].split(", ").map { it.toInt() }
                gv[0].toInt() to values
            }.toTypedArray()
            mapOf(*massaged)
        }

    fun part1() = allLinks(links, 0).size

    fun part2() = generateSequence((0..1999).toMutableSet()) {
        it.apply { removeAll(allLinks(links, it.first()).toSet()) }
    }.indexOfFirst { it.isEmpty() }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D12.part1()} (${elapsedTime(time)}ms)") // 115
    time = System.nanoTime()
    println("Part 2: ${Y2017D12.part2()} (${elapsedTime(time)}ms)") // 221
}