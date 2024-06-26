package org.gristle.adventOfCode.y2016.d16

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.isOdd

class Y2016D16(private val input: String) : Day {

    private tailrec fun checkSum(a: BooleanArray): String {
        val sum = BooleanArray(a.size / 2) { idx ->
            !(a[idx * 2] xor a[idx * 2 + 1])
        }
        return if (sum.size.isOdd()) {
            val answer = buildString { sum.forEach { append(if (it) '1' else '0') } }
            answer
        } else {
            checkSum(sum)
        }
    }

    private tailrec fun dragonCurve(a: BooleanArray, diskSize: Int): BooleanArray {
        return if (a.size >= diskSize) {
            a.sliceArray(0..diskSize)
        } else {
            val b = BooleanArray(a.size * 2 + 1) { idx ->
                if (idx < a.size) {
                    a[idx]
                } else if (idx > a.size) {
                    !a[a.size * 2 - idx]
                } else false
            }
            dragonCurve(b, diskSize)
        }
    }

    fun solve(diskSize: Int) = checkSum(dragonCurve(input.map { it == '1' }.toBooleanArray(), diskSize))

    override fun part1() = solve(272)

    override fun part2() = solve(35651584)
}

// String-based times: (creation, 9ms; pt1, 1ms; pt2, 4321ms)
fun main() = Day.runDay(Y2016D16::class) // 10010101010011101, 01100111101101111

//    Class creation: 16ms
//    Part 1: 10010101010011101 (0ms)
//    Part 2: 01100111101101111 (245ms)
//    Total time: 262ms