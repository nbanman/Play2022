package org.gristle.adventOfCode.y2017.d21

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.*

class Y2017D21(private val input: String) : Day {
    data class Rule(val patternWithDashes: String, private val replacementWithDashes: String) {
        val pattern = patternWithDashes.replace("/", "")
        private val replacement = replacementWithDashes.replace("/", "")
        private val patternWidth = patternWithDashes.length - pattern.length + 1
        private val patternGrid = pattern.toGrid(patternWidth)
        val replacementGrid = replacement.toGrid(patternWidth + 1)
        private val flips = listOf(patternGrid, patternGrid.flipX())
        private val rotateFunctions = listOf<(Grid<Char>) -> Grid<Char>>(
            Grid<Char>::rotate90,
            Grid<Char>::rotate90,
            Grid<Char>::rotate90
        )
        val permutations = flips.flatMap { flip ->
            rotateFunctions
                .runningFold(flip) { acc, rotation -> rotation(acc) }
        }
    }

    private val twos: Map<Grid<Char>, Grid<Char>>
    private val threes: Map<Grid<Char>, Grid<Char>>

    init {
        val (twoRules, threeRules) = input.lines()
            .map { line ->
                line
                    .split(" => ")
                    .let {
                        Rule(it.first(), it.last())
                    }
            }.partition { it.patternWithDashes.length == 5 }

        twos = twoRules
            .flatMap { rule ->
                rule.permutations.map { it to rule.replacementGrid }
            }.toMap()

        threes = threeRules
            .flatMap { rule ->
                rule.permutations.map { it to rule.replacementGrid }
            }.toMap()
    }

    private fun expandGrid(
        grid: Grid<Char>,
        twos: Map<Grid<Char>, Grid<Char>>,
        threes: Map<Grid<Char>, Grid<Char>>
    ): Grid<Char> {
        val (square, rules) = if (grid.width % 2 == 0) 2 to twos else 3 to threes
        val subGrids = mutableListOf<Grid<Char>>()
        for (y in 0 until grid.height step square) {
            for (x in 0 until grid.width step square) {
                subGrids.add(grid.subGrid(Coord(x, y), square, square))
            }
        }
        val transformedSubs = subGrids.map { transGrid ->
            rules[transGrid] ?: throw Exception("no rule matches transGrid")
        }
        return transformedSubs
            .chunked(kotlin.math.sqrt(transformedSubs.size.toDouble()).toInt())
            .map { it.reduce { acc, g -> acc.addRight(g) } }
            .let { it.reduce { acc, g -> acc.addDown(g) }}
    }

    fun solve(iterations: Int): Int {
        val initial = ".#...####".toGrid(3)
        return (1..iterations).fold(initial) { acc, _ ->
            expandGrid(acc, twos, threes)
        }.count { it == '#' }
    }

    override fun part1() = solve(5)

    override fun part2() = solve(18)
}

fun main() = Day.runDay(21, 2017, Y2017D21::class)

//    Class creation: 11ms
//    Part 1: 150 (22ms)
//    Part 2: 2606275 (46194ms)
//    Total time: 46228ms