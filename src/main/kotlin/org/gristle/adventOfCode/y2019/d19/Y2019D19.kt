package org.gristle.adventOfCode.y2019.d19

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

class Y2019D19(input: String) {

    private val initialState = input.split(',').map { it.toLong() }
    private val toDroid: Deque<Long> = LinkedList()
    private val toComp: Deque<Long> = LinkedList()
    private val intCode = IntCode("A", initialState, null, toComp, toDroid)

    fun part1(): Int {
        for (y in 0L..49L) for (x in 0L..49L) {
            toComp.add(x)
            toComp.add(y)
            intCode.reset()
            intCode.run()
        }
        return toDroid.count { it == 1L }
    }
    
    fun part2(): Long {
        // Part 2
        toDroid.clear()
        var rightEdge = 0L
        var leftEdge = 0L
        var y = 0L
        val width = 100
        while (leftEdge + width - 1 != rightEdge) {
            y++
            var rightAdd = 2
            while (true) {
                toComp.add(rightEdge + rightAdd)
                toComp.add(y)
                intCode.reset()
                intCode.run()
                if (toDroid.poll() == 0L) {
                    rightEdge += rightAdd - 1
                    break
                }
                rightAdd++
            }
            var leftAdd = 1
            while (true) {
                toComp.add(leftEdge + leftAdd)
                toComp.add(y + width - 1)
                intCode.reset()
                intCode.run()
                if (toDroid.poll() == 1L) {
                    leftEdge += leftAdd
                    break
                }
                leftAdd++
            }
        }
        return leftEdge * 10_000 + y
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D19(readRawInput("y2019/d19"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 179
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 9760485
}