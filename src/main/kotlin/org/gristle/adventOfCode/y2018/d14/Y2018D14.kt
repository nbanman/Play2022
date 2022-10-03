package org.gristle.adventOfCode.y2018.d14

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2018D14(input: String) {
    private val inputNum = input.toInt()

    fun solve(): Pair<String, Int> {
        var size = 2
        val recipes = MutableList(30_000_000) { -1 }
        recipes[0] = 3
        recipes[1] = 7
        var elves = listOf(0, 1)
        while (recipes.subList(maxOf(size - 7, 0), size - 1).joinToString("").toInt() != inputNum && size < recipes.size) {
            val (ten, one) = elves.sumOf { recipes[it] }.let { it / 10 to it % 10 }
            if (ten != 0) {
                recipes[size] = ten
                size++
            }
            recipes[size] = one
            size++

            elves = elves.map { elf ->
                (elf + (recipes[elf] + 1)) % size
            }
        }
        return recipes.subList(inputNum, inputNum + 10).joinToString("") to size - 7
    }
}

fun main() {
    val time = System.nanoTime()
    val (p1, p2) = Y2018D14(readRawInput("y2018/d14")).solve()
    println("Part 1: $p1") // 4910101614
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 20253137
}