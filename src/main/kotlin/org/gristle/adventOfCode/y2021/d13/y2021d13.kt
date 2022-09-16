package org.gristle.adventOfCode.y2021.d13

import org.gristle.adventOfCode.utilities.*

object Y2021D13 {
    private val input = readRawInput("y2021/d13")

    data class Fold(val axis: Char, val amt: Int) {
        fun execute(paper: Grid<Boolean>): Grid<Boolean> {
            return when (axis) {
                'x' -> {
                    val left = paper.subGrid(Coord(0, 0), amt, paper.height)
                    val right = paper
                        .subGrid(Coord(amt + 1, 0), paper.width - 1 - amt, paper.height)
                        .flipY()
                    val (larger, smaller) = if (left.size > right.size) left to right else right to left
                    val offset = larger.width - smaller.width
                    List(larger.size) { i ->
                        val lCoord = larger.coordOf(i)
                        val sCoord = lCoord + Coord(offset, 0)
                        larger[lCoord] or if (smaller.validCoord(sCoord)) smaller[sCoord] else false
                    }.toGrid(larger.width)
                }
                else -> {
                    val up = paper.subGrid(Coord(0, 0), paper.width, amt)
                    val down = paper
                        .subGrid(Coord(0, amt + 1), paper.width, paper.height - 1 - amt)
                        .flipX()
                    val (larger, smaller) = if (up.size > down.size) up to down else down to up
                    val offset = smaller.height - larger.height
                    List(larger.size) { i ->
                        val lCoord = larger.coordOf(i)
                        val sCoord = lCoord + Coord(0, offset)
                        larger[lCoord] or if (smaller.validCoord(sCoord)) smaller[sCoord] else false
                    }.toGrid(larger.width)
                }
            }
        }
    }

    private val dots = input
        .groupValues("""(\d+),(\d+)""") { it.toInt() }
        .map { Coord(it[0], it[1]) }

    private val folds = input
        .groupValues("""fold along ([xy])=(\d+)""")
        .map { Fold(it[0][0], it[1].toInt()) }

    private val paperWidth = dots.maxOf { it.x } + 1
    private val paperHeight = dots.maxOf { it.y } + 1

    private val paper = List(paperWidth * paperHeight) { false }
        .toMutableGrid(paperWidth)
        .apply {
            dots.forEach { dot -> this[dot] = true }
        }.toGrid()

    fun part1() = folds.first().execute(paper)
        .count { it }

    fun part2(): String = folds.fold(paper) { acc, fold ->
        fold.execute(acc)
    }.let { grid ->
        return grid.representation().replace(".", " ")
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D13.part1()} (${elapsedTime(time)}ms)") // 735
    time = System.nanoTime()
    println("Part 2: \n${Y2021D13.part2()} (${elapsedTime(time)}ms)") // UFRZKAUZ
}