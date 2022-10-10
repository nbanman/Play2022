package org.gristle.adventOfCode.y2015.d2

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D2(input: String) {

    data class Box(val l: Int, val w: Int, val h: Int) {
        private val asList = listOf(l, w, h).sorted()
        private val surfaceArea = 2 * l * w + 2 * w * h + 2 * h * l
        private val smallestSideArea = asList.dropLast(1).reduce(Int::times)
        val paperNeeded = surfaceArea + smallestSideArea
        private val cubicVolume = asList.reduce(Int::times)
        private val ribbonToWrap = asList.dropLast(1).sum() * 2
        val ribbonNeeded = cubicVolume + ribbonToWrap
    }

    private val boxes = input
        .groupValues("""(\d+)x(\d+)x(\d+)""", String::toInt)
        .map { Box(it[0], it[1], it[2]) }

    fun part1() = boxes.sumOf(Box::paperNeeded)

    fun part2() = boxes.sumOf(Box::ribbonNeeded)
}


fun main() {
    var time = System.nanoTime()
    val c = Y2015D2(readRawInput("y2015/d2"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1588178
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 3783758
}