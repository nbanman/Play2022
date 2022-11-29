package org.gristle.adventOfCode.y2015.d2

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D2(input: String) {

    data class Box(val l: Int, val w: Int, val h: Int) {
        private fun cubicVolume() = l * w * h
        private fun surfaceArea() = 2 * l * w + 2 * w * h + 2 * h * l
        private fun smallestSideArea() = cubicVolume() / maxOf(l, w, h)
        fun paperNeeded() = surfaceArea() + smallestSideArea()
        private fun ribbonToWrap() = (l + w + h - maxOf(l, w, h)) * 2
        fun ribbonNeeded() = cubicVolume() + ribbonToWrap()
    }

    private val boxes = input
        .groupValues("""(\d+)x(\d+)x(\d+)""", String::toInt)
        .map { Box(it[0], it[1], it[2]) }

    fun part1() = boxes.sumOf(Box::paperNeeded)

    fun part2() = boxes.sumOf(Box::ribbonNeeded)
}


fun main() {
    val timer = Stopwatch(true)
    val c = Y2015D2(readRawInput("y2015/d2"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 1588178
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 3783758
}