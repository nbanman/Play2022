package org.gristle.adventOfCode.y2016.d3

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getInts
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.transpose

private typealias MakeTriangles = (List<List<Int>>) -> List<Y2016D3.Triangle>
class Y2016D3(input: String) {
    data class Triangle(val a: Int, val b: Int, val c: Int) {
        val isValid = let {
            val max = maxOf(a, b, c)
            a + b + c - max > max
        }
    }

    private val trios = input.getInts().chunked(3)

    private inline fun solve(makeTriangles: MakeTriangles) =
        makeTriangles(trios).count(Triangle::isValid)

    fun part1(): Int {
        val makeTriangles: MakeTriangles = { trios -> trios.map { Triangle(it[0], it[1], it[2]) } }
        return solve(makeTriangles)
    }

    fun part2(): Int {
        val makeTriangles: MakeTriangles = { trios ->
            trios
                .transpose()
                .flatMap { lengths -> lengths.chunked(3) { Triangle(it[0], it[1], it[2]) } }
        }
        return solve(makeTriangles)
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