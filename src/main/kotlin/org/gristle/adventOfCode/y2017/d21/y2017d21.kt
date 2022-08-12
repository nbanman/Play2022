package org.gristle.adventOfCode.y2017.d21

import org.gristle.adventOfCode.utilities.*

object Y2017D21 {
    data class Rule(val patternWithDashes: String, private val replacementWithDashes: String) {
        val pattern = patternWithDashes.replace("/", "")
        val replacement = replacementWithDashes.replace("/", "")
        private val patternWidth = patternWithDashes.length - pattern.length + 1
        private val patternGrid = pattern.toGrid(patternWidth)
        private val flips = listOf(patternGrid, patternGrid.flipX())
        private val rotateFunctions = listOf<(Grid<Char>) -> Grid<Char>>(
            Grid<Char>::rotate90,
            Grid<Char>::rotate90,
            Grid<Char>::rotate90
        )
        private val permutations = flips.flatMap {
            rotateFunctions.fold(mutableListOf(it)) { acc, function ->
                acc.apply { add(function(acc.last())) }
            }
        }

        fun matches(g: Grid<Char>) = g in permutations
    }
    private fun expandGrid(grid: Grid<Char>, twoRules: List<Rule>, threeRules: List<Rule>): Grid<Char> {
        val (square, rules) = if (grid.width % 2 == 0) 2 to twoRules else 3 to threeRules
        val subGrids = mutableListOf<Grid<Char>>()
        for (y in 0 until grid.height step square) {
            for (x in 0 until grid.width step square) {
                subGrids.add(grid.subGrid(Coord(x, y), square, square))
            }
        }
        val transformedSubs = subGrids.map { transGrid ->
            val rule = rules.find { it.matches(transGrid) }!!
            rule.replacement.toGrid(square + 1)
        }
        return transformedSubs
            .chunked(kotlin.math.sqrt(transformedSubs.size.toDouble()).toInt())
            .map { it.reduce { acc, g -> acc.addRight(g) } }
            .let { it.reduce { acc, g -> acc.addDown(g) }}
    }
    private val input = readInput("y2017/d21")

    fun solve(iterations: Int): Int {
        val initial = ".#...####".toGrid(3)
        val (twoRules, threeRules) = input
            .map { line ->
                line
                    .split(" => ")
                    .let {
                        Rule(it.first(), it.last())
                    }
            }.partition { it.patternWithDashes.length == 5 }
        return (1..iterations).fold(initial) { acc, _ ->
            expandGrid(acc, twoRules, threeRules)
        }.count { it == '#' }
    }

    fun part1() = solve(5)

    fun part2() = solve(18)
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2017D21.part1()} (${elapsedTime(time)}ms)") // 150
    time = System.nanoTime()
    println("Part 2: ${Y2017D21.part2()} (${elapsedTime(time)}ms)") // 2606275 
}