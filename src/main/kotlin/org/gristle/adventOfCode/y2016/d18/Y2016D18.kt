package org.gristle.adventOfCode.y2016.d18

import org.gristle.adventOfCode.Day

class Y2016D18(input: String) : Day {

    // Convert input to BooleanArray
    private val start = BooleanArray(input.length) { i -> input[i] == '.' }

    // Takes a row and returns the next one using the rules in the problem. 
    private fun BooleanArray.nextRow() = BooleanArray(size) { i ->
        when (i) {
            0 -> this[1] // no previous so answer is true if next is true
            lastIndex -> this[i - 1] // no next so answer is true if previous is true
            else -> this[i - 1] == this[i + 1] // base case: true if previous and next are the same
        }
    }

    // Creates a sequence of the rows and sums the number of true values in each row.
    private fun solve(rows: Int) = generateSequence(start) { it.nextRow() }
        .take(rows)
        .sumOf { row -> row.count { it } }

    override fun part1() = solve(40)

    override fun part2() = solve(400_000)
}

// Times in parentheses are for previous String-based solution. 
// Class creation: 5ms (13ms)
// Part 1: 1987 4ms (7ms)
// Part 2: 19984714 286ms (2295ms)
// Total time: 297ms (2315ms)
fun main() = Day.runDay(Y2016D18::class)