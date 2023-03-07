package org.gristle.adventOfCode.y2017.d9

import org.gristle.adventOfCode.Day

class Y2017D9(private val input: String) : Day {

    val solution: Pair<Int, Int> = let {
        var inGarbage = false
        var garbage = 0
        var depth = 0
        var score = 0
        var last = ' '
        input.forEach { c ->
            if (inGarbage) {
                if (c == '>' && last != '!') inGarbage = false
                if (!(c in "!>" || last == '!')) garbage++
                last = if (last == '!') ' ' else c
            } else {
                when (c) {
                    '<' -> inGarbage = true
                    '{' -> {
                        depth++
                        score += depth
                    }

                    '}' -> {
                        depth--
                    }
                }
            }
        }
        score to garbage
    }

    override fun part1() = solution.first

    override fun part2() = solution.second
}


fun main() = Day.runDay(Y2017D9::class)

//    Class creation: 15ms
//    Part 1: 9251 (0ms)
//    Part 2: 4322 (0ms)
//    Total time: 15ms