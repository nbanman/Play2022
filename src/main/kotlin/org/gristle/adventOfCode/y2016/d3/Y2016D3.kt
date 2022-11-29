package org.gristle.adventOfCode.y2016.d3

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.transpose

class Y2016D3(input: String) {
    data class Triangle(val a: Int, val b: Int, val c: Int) {
        private val asList = listOf(a, b, c).sorted()
        val isValid = asList[0] + asList[1] > asList[2]
    }

    private val trios = input.getInts().chunked(3)

    private inline fun solve(makeTriangles: (List<List<Int>>) -> List<Triangle>) =
        makeTriangles(trios).count(Triangle::isValid)

    fun part1() = solve { trios -> trios.map { Triangle(it[0], it[1], it[2]) } }

    fun part2() = solve { trios ->
        trios
            .transpose()
            .flatMap { lengths -> lengths.chunked(3) { Triangle(it[0], it[1], it[2]) } }
    }
}

fun main() {
    val timer = Stopwatch(true)
    val c = Y2016D3(readRawInput("y2016/d3"))
    println("Class creation: ${timer.lap()}ms")
    println("Part 1: ${c.part1()} (${timer.lap()}ms)") // 1032
    println("Part 2: ${c.part2()} (${timer.lap()}ms)") // 1838
    println("Total time: ${timer.elapsed()}ms")
}