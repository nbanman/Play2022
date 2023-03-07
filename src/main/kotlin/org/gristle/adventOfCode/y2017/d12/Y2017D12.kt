package org.gristle.adventOfCode.y2017.d12

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.groupValues

class Y2017D12(input: String) : Day {

    companion object {
        fun allLinks(links: Map<Int, List<Int>>, seed: Int): List<Int> {

            tailrec fun aL(found: Set<Int>, evaluate: Set<Int>): Set<Int> {
                return if (evaluate.isEmpty()) {
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
        .associate { it[0].toInt() to it[1].getInts().toList() }

    override fun part1() = allLinks(links, 0).size

    override fun part2() = generateSequence((0..1999).toMutableSet()) {
        it.apply { removeAll(allLinks(links, it.first()).toSet()) }
    }.indexOfFirst { it.isEmpty() }
}

fun main() = Day.runDay(Y2017D12::class)

//    Class creation: 66ms
//    Part 1: 115 (2ms)
//    Part 2: 221 (15ms)
//    Total time: 83ms