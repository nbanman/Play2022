package org.gristle.adventOfCode.y2016.d20

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

// Not refactored; ugly but fast
class Y2016D20(input: String) {

    data class LRange(val start: Long, val end: Long)

    val p1 = input
        .groupValues("""(\d+)-(\d+)""")
        .map { LRange(it[0].toLong(), it[1].toLong()) }
        .sortedBy { it.start }

    fun part1(): Long {
        var push = 0L
        for (range in p1) {
            if (push < range.start) {
                return push
            }
            push = maxOf(push, range.end + 1L)
        }
        return -1
    }

    fun part2(): Long {
        var push = 0L
        var ips = 0L
        for (range in p1) {
            if (push < range.start) {
                ips += range.start - push
            }
            push = maxOf(push, range.end + 1L)
        }
        return ips
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D20(readRawInput("y2016/d20"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 19449262
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 119
}