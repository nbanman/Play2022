package org.gristle.adventOfCode.y2023.d13

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.toGrid

class Y2023D13(input: String) : Day {

    private val patterns: List<Pair<Grid<Boolean>, Int>> by lazy {
        input.blankSplit()
            .map { s ->
                val pattern = s.toGrid { it == '#' }
                with(pattern) {
                    val value = findSeam(width, columns(), 1)
                        ?: findSeam(height, rows(), 100)
                        ?: throw IllegalStateException("No seam found in columns or rows.")
                    pattern to value
                }
            }
    } 
    

    private fun Grid<Boolean>.findSeam(length: Int, lines: List<List<Boolean>>, factor: Int): Int? {
        val stack: MutableList<List<Boolean>> = ArrayList(length - 1)
        stack.add(lines.first())
        var potentialSeam = -1
        var location = 1
        loop@while (location in lines.indices) {
            val next = lines[location]
            if (next != stack.last()) {
                stack.add(next)
                location++
            } else { // potential seam found
                potentialSeam = location
                inner@while (true) {
                    stack.removeLast()
                    if (stack.isEmpty()) break@loop
                    val next = lines.getOrNull(++location) ?: break@loop
                    if (stack.last() != next) { // potential seam not real
                        location = potentialSeam - (location - potentialSeam)
                        while (location != potentialSeam + 1) {
                            stack.add(lines[location])
                            location++
                        }
                        potentialSeam = -1
                        break@inner
                    }
                }
                
            }
        }
        return if (potentialSeam == -1) null else potentialSeam * factor
    }

    private fun Grid<Boolean>.findSmudgeSeam(length: Int, lines: List<List<Boolean>>, factor: Int, ignore: Int?): Int? {
        val stack: MutableList<List<Boolean>> = ArrayList(length - 1)
        stack.add(lines.first())
        var potentialSeam = -1
        var location = 1
        var smudged = false
        loop@while (location in lines.indices) {
            val next = lines[location]
            val difference = next.difference(stack.last())
            if (difference > 1 || (difference == 0 && ignore == location)) {
                stack.add(next)
                location++
            } else { // potential seam found
                if (difference == 1) smudged = true
                potentialSeam = location
                inner@while (true) {
                    stack.removeLast()
                    if (stack.isEmpty()) break@loop
                    val innerNext = lines.getOrNull(++location) ?: break@loop
                    val innerDifference = innerNext.difference(stack.last())
                    if (innerDifference > 1 || (innerDifference == 1 && smudged)) { // potential seam not real
                        smudged = false
                        location = potentialSeam - (location - potentialSeam)
                        while (location != potentialSeam + 1) {
                            stack.add(lines[location])
                            location++
                        }
                        potentialSeam = -1
                        break@inner
                    }
                    if (innerDifference == 1) smudged = true
                }

            }
        }
        return if (potentialSeam == -1) null else potentialSeam * factor
    }
    
    private fun List<Boolean>.difference(other: List<Boolean>): Int {
        return (this zip other).count { (a, b) -> a != b }
    }

    override fun part1() = patterns.sumOf { (_, value) -> value }

    override fun part2() = patterns
        .mapIndexed { index, (pattern, value) ->
            val ignoreHorizontal = if (value < 100) value else null
            val ignoreVertical = if (value >= 100) value / 100 else null
            with (pattern) {
                val answer = findSmudgeSeam(width, columns(), 1, ignoreHorizontal)
                    ?: findSmudgeSeam(height, rows(), 100, ignoreVertical)
                    ?: throw IllegalStateException("No seam found in columns or rows.")
                answer
            }
        }.sum()
    
    companion object {
        fun List<Boolean>.rep() = joinToString("") { if (it) "#" else "."}
    }
}

fun main() = Day.runDay(Y2023D13::class)

@Suppress("unused")
private val sampleInput = listOf(
    """#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
""",
)