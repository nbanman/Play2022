package org.gristle.adventOfCode.y2021.d13

import org.gristle.adventOfCode.utilities.*

class Y2021D13(input: String) {

    data class FoldInstruction(val axis: Char, val amt: Int) {
        fun execute(paper: Grid<Boolean>): Grid<Boolean> {
            return when (axis) {
                'x' -> {
                    val left = paper.subGrid(Coord(0, 0), amt, paper.height)
                    val right = paper
                        .subGrid(Coord(amt + 1, 0), paper.width - 1 - amt, paper.height)
                        .flipY()
                    val (larger, smaller) = largerSmaller(left, right)
                    val offset = larger.width - smaller.width
                    performFold(Coord(offset, 0), larger, smaller)
                }
                else -> {
                    val up = paper.subGrid(Coord(0, 0), paper.width, amt)
                    val down = paper
                        .subGrid(Coord(0, amt + 1), paper.width, paper.height - 1 - amt)
                        .flipX()
                    val (larger, smaller) = largerSmaller(up, down)
                    val offset = smaller.height - larger.height
                    performFold(Coord(0, offset), larger, smaller)
                }
            }
        }

        private fun largerSmaller(a: Grid<Boolean>, b: Grid<Boolean>) = if (a.size > b.size) a to b else b to a

        private fun performFold(
            adjustment: Coord,
            larger: Grid<Boolean>,
            smaller: Grid<Boolean>,
        ) = List(larger.size) { i ->
            val lCoord = larger.coordOf(i)
            val sCoord = lCoord + adjustment
            larger[lCoord] or if (smaller.validCoord(sCoord)) smaller[sCoord] else false
        }.toGrid(larger.width)
    }

    private val foldInstructions = input
        .groupValues("""fold along ([xy])=(\d+)""")
        .map { FoldInstruction(it[0][0], it[1].toInt()) }

    private val dots = input
        .groupValues("""(\d+),(\d+)""") { it.toInt() }
        .map { Coord(it[0], it[1]) }

    private val paperWidth = dots.maxOf { it.x } + 1
    private val paperHeight = dots.maxOf { it.y } + 1

    private val paper = MutableGrid(paperWidth, paperHeight) { false }
        .apply {
            dots.forEach { dot -> this[dot] = true }
        } as Grid<Boolean>

    fun part1() = foldInstructions.first().execute(paper).count { it }

    fun part2(): String = foldInstructions
        .fold(paper) { acc, foldInstruction -> foldInstruction.execute(acc) }
        .ocr()
}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D13(readRawInput("y2021/d13"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 735
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // UFRZKAUZ
}