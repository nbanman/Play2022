package org.gristle.adventOfCode.y2017.d12

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D12(input: String) {

    companion object {
        fun allLinks(links: Map<Int, List<Int>>, seed: Int): List<Int> {

            tailrec fun aL(found: Set<Int>, evaluate: Set<Int>): Set<Int> {
                return if(evaluate.isEmpty()) {
                    found
                } else {
                    val newFound = found + evaluate
                    val newRegisters = evaluate.fold(listOf<Int>()) { acc, i ->
                        acc + links.getValue(i).filter { it !in newFound }
                    }.toSet()
                    aL(found + evaluate, newRegisters)
                }
            }
            return aL(emptySet(), setOf(seed)).toList()
        }
    }

    private val pattern = """(\d+) <-> (\d+(?:, \d+)*)""".toRegex()

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
    val c = Y2017D12(readRawInput("y2017/d12"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 115
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 221
}