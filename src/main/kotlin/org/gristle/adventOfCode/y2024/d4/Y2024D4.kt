package org.gristle.adventOfCode.y2024.d4

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.toGrid

class Y2024D4(input: String) : Day {
    val jumble = input.toGrid()
    override fun part1(): Int {
        val starts = jumble.withIndex().filter { (_, c) -> c == 'X' }.map { (idx) -> jumble.coordOf(idx) }
        return starts
            .flatMap { start ->
                Coord.ALL_ADJACENT
                    .map { dir ->
                        (1..3)
                            .runningFold(start) { acc, _ -> acc + dir }
                            .mapNotNull { pos -> jumble.getOrNull(pos) }
                            .joinToString("")
                    }
            }
            .count { it == "XMAS" }
    }
    override fun part2(): Int {
        val starts = jumble.withIndex().filter { (_, c) -> c == 'A' }.map { (idx) -> jumble.coordOf(idx) }
        val lr = listOf(Coord(-1, -1), Coord.ORIGIN, Coord(1, 1))
        val rl = listOf(Coord(1, -1), Coord.ORIGIN, Coord(-1, 1))
        return starts.count { start ->
            val lMas = lr.map { pos -> jumble.getOrNull(start + pos) }.joinToString("")
            val rMas = rl.map { pos -> jumble.getOrNull(start + pos) }.joinToString("")
            (lMas == "MAS" || lMas == "SAM") && (rMas == "MAS" || rMas == "SAM")
        }
    }
}

fun main() = Day.runDay(Y2024D4::class)

@Suppress("unused")
private val test = listOf(
    """MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX""",
)