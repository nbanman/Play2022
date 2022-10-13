package org.gristle.adventOfCode.y2017.d24

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

private typealias Bridge = List<Y2017D24.MagComp>

class Y2017D24(input: String) {

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
        remaining: List<MagComp> = components,
    ): Bridge {
        return remaining
            .filter { it.canJoin(n) }
            .map { buildBridge(comparator, it.otherEnd(n), bridge + it, remaining - it) }
            .maxWithOrNull(comparator) ?: bridge
    }

    private val components = input
        .groupValues("""(\d+)\/(\d+)""", String::toInt)
        .map { MagComp(it[0], it[1]) }

    private val compareByStrength = compareBy { bridge: Bridge -> bridge.strength() }
    fun part1() = buildBridge(compareByStrength).strength()
    fun part2() = buildBridge(compareBy(Bridge::size) then compareByStrength).strength()
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D24(readRawInput("y2017/d24"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1868 (638ms custom)
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 1841 (533ms custom)
}