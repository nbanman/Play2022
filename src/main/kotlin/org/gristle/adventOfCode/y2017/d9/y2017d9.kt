package org.gristle.adventOfCode.y2017.d9

import org.gristle.adventOfCode.utilities.*

object Y2017D9 {
    private val input = readRawInput("y2017/d9")

    fun solve(): Pair<Int, Int> {
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
        return score to garbage
    }
}

fun main() {
    var time = System.nanoTime()
    val (p1, p2) = Y2017D9.solve()
    println("Part 1: $p1\nPart 2: $p2 (${elapsedTime(time)}ms)") // 9251, 4322
}