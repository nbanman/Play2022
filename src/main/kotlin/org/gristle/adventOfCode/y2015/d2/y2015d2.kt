package org.gristle.adventOfCode.y2015.d2

import org.gristle.adventOfCode.utilities.*

object Y2015D2 {
    private val input = readRawInput("y2015/d2")

    data class Box(val l: Int, val w: Int, val h: Int) {
        private val asList = listOf(l, w, h).sorted()
        private val surfaceArea = 2 * l * w + 2 * w * h + 2 * h * l
        private val smallestSideArea = asList.dropLast(1).reduce { acc, i -> acc * i }
        val paperNeeded = surfaceArea + smallestSideArea
        private val cubicVolume = asList.reduce { acc, i -> acc * i }
        private val ribbonToWrap = asList.dropLast(1).sum() * 2
        val ribbonNeeded = cubicVolume + ribbonToWrap
    }

    private val boxes = input
        .groupValues("""(\d+)x(\d+)x(\d+)""") { it.toInt() }
        .map { Box(it[0], it[1], it[2]) }

    fun part1() = boxes.sumOf { it.paperNeeded }

    fun part2() = boxes.sumOf { it.ribbonNeeded }
}


fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D2.part1()} (${elapsedTime(time)}ms)") // 1588178
    time = System.nanoTime()
    println("Part 2: ${Y2015D2.part2()} (${elapsedTime(time)}ms)") // 3783758
}