package org.gristle.adventOfCode.y2015.d2

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.runDay

class Y2015D2(input: String) : Day {

    private data class Box(val l: Int, val w: Int, val h: Int) {
        private fun cubicVolume() = l * w * h
        private fun surfaceArea() = 2 * l * w + 2 * w * h + 2 * h * l
        private fun smallestSideArea() = cubicVolume() / maxOf(l, w, h)
        fun paperNeeded() = surfaceArea() + smallestSideArea()
        private fun ribbonToWrap() = (l + w + h - maxOf(l, w, h)) * 2
        fun ribbonNeeded() = cubicVolume() + ribbonToWrap()
    }

    private val boxes = input
        .getInts()
        .chunked(3)
        .map { (l, w, h) -> Box(l, w, h) }

    override fun part1() = boxes.sumOf(Box::paperNeeded)

    override fun part2() = boxes.sumOf(Box::ribbonNeeded)
}

fun main() {
    runDay(2, 2015, Y2015D2::class)
} // 1588178, 3783758