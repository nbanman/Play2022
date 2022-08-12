package org.gristle.adventOfCode.y2019.d4

import org.gristle.adventOfCode.utilities.*

object Y2019D4 {
    private val input = readRawInput("y2019/d4")

    private val range = input.split('-').let { it[0].toInt()..it[1].toInt() }

    private inline fun CharSequence.allIndexed(predicate: (index: Int, Char) -> Boolean): Boolean {
        forEachIndexed { index, c -> if (!predicate(index, c)) return false }
        return true
    }

    fun part1() = range.count { password ->
        val passString = password.toString()
        val zippedPassString = passString.zipWithNext()
        passString.allIndexed { index, c -> index == 0 || passString[index - 1] <= c }
                && zippedPassString.all { it.first <= it.second }
                && zippedPassString.any { it.first == it.second }
    }

    val pattern = Regex("""(\d)\1+""")

    fun part2() = range.count { password ->
        val passString = password.toString()
        passString.allIndexed { index, c -> index == 0 || passString[index - 1] <= c }
                && pattern.findAll(passString).any { it.value.length == 2 }
    }

}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D4.part1()} (${elapsedTime(time)}ms)") // 466
    time = System.nanoTime()
    println("Part 2: ${Y2019D4.part2()} (${elapsedTime(time)}ms)") // 292
}