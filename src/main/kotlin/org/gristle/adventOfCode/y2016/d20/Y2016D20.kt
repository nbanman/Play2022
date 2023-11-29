package org.gristle.adventOfCode.y2016.d20

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getLongList
import kotlin.math.max

class Y2016D20(input: String) : Day {

    // parse input into ranges and sort the ranges by the start of the range. Using Long instead of UInt to 
    // avoid overflow issues.
    private val ranges = input
        .getLongList(omitDashes = true)
        .chunked(2) { (start, end) -> start..end }
        .sortedBy(LongRange::first)

    // delivers a sequence of ips not blocked by the whitelist by starting at 0
    private val ipSequence = sequence {
        var ip = 0L // start at zero
        ranges.forEach { range -> // for each range...
            (ip until range.first).forEach { yield(it) } // yield all values b/t ip and range.first
            ip = max(ip, range.last + 1) // ip cannot be w/in range so bump ip to the end of the range + 1
        }

        // if last range did not go to 4294967295, this would continue to yield numbers until that max was reached
        yieldAll(ip..4294967295L)
    }

    override fun part1() = ipSequence.first() // gets first (i.e., lowest) unblocked ip

    override fun part2() = ipSequence.count() // counts total unblocked ips
}

fun main() = Day.runDay(Y2016D20::class)

//Class creation: 34ms
//Part 1: 19449262 (2ms)
//Part 2: 119 (0ms)
//Total time: 37ms