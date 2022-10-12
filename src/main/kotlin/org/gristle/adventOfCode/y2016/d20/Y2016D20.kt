package org.gristle.adventOfCode.y2016.d20

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.max

class Y2016D20(input: String) {

    // parse input into ranges and sort the ranges by the start of the range. Using Long instead of UInt to 
    // avoid overflow issues.
    private val ranges = input
        .groupValues("""(\d+)-(\d+)""", String::toLong)
        .map { LongRange(it[0], it[1]) }
        .sortedBy(LongRange::first)

    // delivers a sequence of ips not blocked by the whitelist by starting at 0
    private val ipSequence = sequence {
        var ip = 0L // start at zero
        ranges.forEach { range -> // for each range...
            (ip until range.first).forEach { yield(it) } // yield all values b/t ip and range.first
            ip = max(ip, range.last + 1) // ip cannot be w/in range so bump ip to the end of the range + 1
        }
        // if last range did not go to 4294967295, this would continue to yield numbers until that max was reached
        yieldAll(LongRange(ip, UInt.MAX_VALUE.toLong()))
    }

    fun part1() = ipSequence.first() // gets first (i.e., lowest) unblocked ip

    fun part2() = ipSequence.count() // counts total unblocked ips
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