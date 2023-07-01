package org.gristle.adventOfCode.y2018.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.getPairSequence

class Y2018D2(input: String) : Day {

    private val boxIds = input.lines()

    override fun part1(): Int {
        val frequencies = boxIds.map { it.eachCount().values }
        return frequencies.count { it.contains(2) } * frequencies.count { it.contains(3) }
    }

    override fun part2(): String {
        fun Pair<String, String>.differsByOne(): Boolean {
            var diffs = false
            for (idx in first.indices) {
                if (first[idx] != second[idx]) {
                    if (!diffs) {
                        diffs = true
                    } else {
                        return false
                    }
                }
            }
            return diffs
        }

        fun Pair<String, String>.shared() = buildString {
            this@shared.first.forEachIndexed { index, a ->
                if (a == this@shared.second[index]) append(a)
            }
        }

        return boxIds
            .getPairSequence()
            .first { it.differsByOne() }
            .shared()
    }
}

fun main() = Day.runDay(Y2018D2::class)

//    Class creation: 9ms
//    Part 1: 7688 (8ms)
//    Part 2: lsrivmotzbdxpkxnaqmuwcchj (14ms)
//    Total time: 31ms