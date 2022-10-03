package org.gristle.adventOfCode.y2017.d11

import org.gristle.adventOfCode.utilities.Hexagon
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D11(input: String) {
    private val dirs = input.split(',')

    fun solve(): Pair<Int, Int> {
        val home = Hexagon()
        return dirs
            .fold(home to 0) { (hex, furthest), step ->
                val intermediate = hex.hexAt(step)
                intermediate to maxOf(furthest, intermediate.distance(home))
            }.let { (hex, furthest) -> hex.distance(home) to furthest }
    }
}

fun main() {
    val time = System.nanoTime()
    val (part1, part2) = Y2017D11(readRawInput("y2017/d11")).solve()
    println("Part 1: $part1") // 747
    println("Part 2: $part2 (${elapsedTime(time)}ms)") // 1544
}