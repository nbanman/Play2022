package org.gristle.adventOfCode.y2016.d7

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2016D7(input: String) {
    private val lines = input.lines()

    data class IPv7(val supernets: List<String>, val hypernets: List<String>) {
        private val pattern = """(\w)(?!\1)(\w)\2\1""".toRegex()
        val supportsTls = supernets.any { pattern.containsMatchIn(it) }
                && hypernets.none { pattern.containsMatchIn(it) }
        val supportsSsl = let {
            val abaPattern = """(?=((\w)(?!\2)(\w)\2))."""
            val abas = supernets.map { supernet -> supernet.groupValues(abaPattern).map { it[0] } }.flatten()
            abas.any { aba ->
                val bab = "${aba[1]}${aba[0]}${aba[1]}"
                hypernets.any { it.contains(bab) }
            }
        }
    }

    val pattern = """\[?\w+]?""".toRegex()

    private val addresses = lines.map { line ->
        val (hyper, standard) = pattern.findAll(line).map { it.value }.partition { it.firstOrNull() == '[' }
        IPv7(standard, hyper.map { it.drop(1).dropLast(1) })
    }

    fun part1() = addresses.count { it.supportsTls }

    fun part2() = addresses.count { it.supportsSsl }

}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D7(readRawInput("y2016/d7"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 118
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 260
}