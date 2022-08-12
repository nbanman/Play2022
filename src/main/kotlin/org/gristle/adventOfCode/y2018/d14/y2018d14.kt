package org.gristle.adventOfCode.y2018.d14

import org.gristle.adventOfCode.utilities.*

object Y2018D14 {
    private val input = 793031

    fun solve(): Pair<String, Int> {
        val input = 793031
        var size = 2
        val recipes = MutableList(30_000_000) { -1 }
        recipes[0] = 3
        recipes[1] = 7
        var elves = listOf(0, 1)
        while (recipes.subList(maxOf(size - 7, 0), size - 1).joinToString("").toInt() != input && size < recipes.size) {
            val (ten, one) = elves.sumBy { recipes[it] }.let { it / 10 to it % 10 }
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
        return recipes.subList(input, input + 10).joinToString("") to size - 7
    }
}

fun main() {
    val time = System.nanoTime()
    val (p1, p2) = Y2018D14.solve()
    println("Part 1: $p1") // 4910101614
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 20253137
}