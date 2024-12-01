package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.MutableGrid
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.toMutableGrid

fun main() {
    val (input1, input2, input3) = ecInputs(24, 10)
    val timer = Stopwatch(true)
    println("1. ${solve(input1) { joinToString("") }}: ${timer.lap()}ms")
    println("2. ${solve(input2) { map(String::power).sum() } }: ${timer.lap()}ms")
    println("3. ${solve(input3) { map(String::power).sum() } }: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private data class Sample(
    val tl: Coord,
    val hz: List<List<Coord>>,
    val vt: List<List<Coord>>,
) {
    companion object {
        private val offsets = listOf(0, 1, 6, 7)
        fun of(pos: Coord): Sample {
            val hz = (2 until 6)
                .map { yOffset ->
                    offsets.map { xOffset -> Coord(pos.x + xOffset, pos.y + yOffset) }
                }
            val vt = (2 until 6)
                .map { xOffset ->
                    offsets.map { yOffset -> Coord(pos.x + xOffset, pos.y + yOffset) }
                }
            return Sample(pos + 2, hz, vt)
        }
    }
}

private fun<T> solve(input: String, output: Iterable<String>.() -> T): T {
    val wall: MutableGrid<Char> = input.replace("\n\n", "\n").toMutableGrid()
    val samples: List<Sample> = wall.getSamples()
    wall.deduceRunes(samples)
    return samples
        .map { pos -> wall.getRunicWord(pos) }
        .output()
}

private fun MutableGrid<Char>.getRunicWord(pos: Sample): String {
    val pos = pos.tl
    return Coord
        .rectangleFrom(pos, pos + 3)
        .map { this@getRunicWord[it] }
        .joinToString("")
}

private fun MutableGrid<Char>.deduceRunes(sampleCoords: Iterable<Sample>) {
    var changed = true
    while (changed) {
        changed = false
        for ((tl, hz, vt) in sampleCoords) {
            val hzLists = hz.map { row -> row.map { pos -> this[pos] } }
            val vtLists = vt.map { col -> col.map { pos -> this[pos] } }

            for (runeSpot in Coord.rectangleFrom(Coord.ORIGIN, Coord(3, 3))) {
                val rune = this[runeSpot + tl]
                val hzList = hzLists[runeSpot.y]
                val vtList = vtLists[runeSpot.x]
                if (!rune.isLetter()) {
                    val intersect = hzList.intersect(vtList).filter { it.isLetter() }
                    if (intersect.isNotEmpty()) {
                        this[runeSpot + tl] = intersect.first()
                        changed = true
                    } else {
                        if (hzList.all { it.isLetter() } && vtList.count { it.isLetter() } == 3) {
                            val runeRow = (0 until 4)
                                .map { xOffset -> this[Coord(tl.x + xOffset, tl.y + runeSpot.y)] }
                                .toSet()
                            if (runeRow.count { it.isLetter() } == 3) {
                                val rune = hzList.minus(runeRow).filter { it.isLetter() }.firstOrNull()
                                if (rune != null) {
                                    this[runeSpot + tl] = rune
                                    val cross = vtLists[runeSpot.x].indexOfFirst { it == '?' }
                                    this[vt[runeSpot.x][cross]] = rune
                                    changed = true
                                }
                            }
                        } else if (vtList.all { it.isLetter() } && hzList.count { it.isLetter() } == 3) {
                            val runeCol = (0 until 4)
                                .map { yOffset -> this[Coord(tl.x + runeSpot.x, tl.y + yOffset)] }
                                .toSet()
                            if (runeCol.count { it.isLetter() } == 3) {
                                val rune = vtList.minus(runeCol).filter { it.isLetter() }.first()
                                this[runeSpot + tl] = rune
                                val cross = hzLists[runeSpot.y].indexOfFirst { it == '?' }
                                this[hz[runeSpot.y][cross]] = rune
                                changed = true
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Grid<Char>.getSamples(): List<Sample> {
    val row = row(2)
    val col = column(2)
    val samplesPerRow = row.count { it == '.' } / 4
    val samplesPerCol = col.count { it == '.' } / 4
    val rowSpacing = when (row.drop(8).indexOf('.')) {
        -1 -> 0
        0 -> 6
        else -> 9
    }

    val colSpacing = when (col.drop(8).indexOf('.')) {
        -1 -> 0
        0 -> 6
        else -> 8
    }
    return (0 until samplesPerCol).flatMap { y ->
        (0 until samplesPerRow).map { x -> Sample.of(Coord(x * rowSpacing, y * colSpacing)) }
    }
}

private fun String.power(): Int =
    if (this.all { it.isLetter() }) {
        this.mapIndexed { idx, c -> (c.code - 64) * (idx + 1) }.sum()
    } else {
        0
    }