package org.gristle.adventOfCode.y2017.d24

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts

private typealias Bridge = List<Y2017D24.MagComp>

class Y2017D24(input: String) : Day {

    data class MagComp(val a: Int = 0, val b: Int = 0) {
        val strength = a + b
        fun canJoin(n: Int) = n == a || n == b
        fun otherEnd(n: Int) = if (n == a) b else a
    }

    private fun Bridge.strength() = sumOf(MagComp::strength)

    private fun buildBridge(
        comparator: Comparator<Bridge>,
        n: Int = 0,
        bridge: Bridge = listOf(),
        remaining: Bridge = components,
    ): Bridge {
        return remaining
            .filter { it.canJoin(n) }
            .map { buildBridge(comparator, it.otherEnd(n), bridge + it, remaining - it) }
            .maxWithOrNull(comparator) ?: bridge
    }

    private val components = input
        .getInts()
        .chunked(2) { MagComp(it[0], it[1]) }
        .toList()

    private val compareByStrength = compareBy { bridge: Bridge -> bridge.strength() }

    override fun part1() = buildBridge(compareByStrength).strength()
    override fun part2() = buildBridge(compareBy(Bridge::size) then compareByStrength).strength()
}

fun main() = Day.runDay(Y2017D24::class)

//    Class creation: 17ms
//    Part 1: 1868 (602ms)
//    Part 2: 1841 (589ms)
//    Total time: 1209ms