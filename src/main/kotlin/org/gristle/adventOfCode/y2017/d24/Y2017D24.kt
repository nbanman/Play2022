package org.gristle.adventOfCode.y2017.d24

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

private typealias Bridge = List<Y2017D24.MagComp>

// not refactored
class Y2017D24(input: String) {

    data class MagComp(val a: Int = 0, val b: Int = 0) {
        val strength = a + b
        fun canJoin(n: Int) = n == a || n == b
        fun otherEnd(n: Int) = if (n == a) b else a
    }

    private fun Bridge.value() = sumOf { it.strength }

    private fun buildBridge(
        n: Int,
        bridge: Bridge,
        remaining: List<MagComp>,
        comparator: Comparator<Bridge>
    ): Bridge {
        return remaining
            .filter { it.canJoin(n) }
            .map { buildBridge(it.otherEnd(n), bridge + it, remaining - it, comparator)}
            .maxWithOrNull(comparator) ?: bridge
    }

    private val components = input
        .groupValues("""(\d+)\/(\d+)""")
        .map { MagComp(it[0].toInt(), it[1].toInt()) }
        .toList()

    fun part1() = buildBridge(0, listOf(), components, compareBy { it.value() }).value()

    fun part2() = buildBridge(
        0,
        listOf(),
        components,
        compareBy(Bridge::size) then compareBy { it.value() }
    ).value()
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D24(readRawInput("y2017/d24"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1868
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1841
}