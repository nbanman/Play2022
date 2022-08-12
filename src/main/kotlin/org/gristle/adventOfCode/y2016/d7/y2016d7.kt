package org.gristle.adventOfCode.y2016.d7

import org.gristle.adventOfCode.utilities.*

object Y2015D7 {
    private val lines = readInput("y2016/d7")

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

    val pattern = """\[?\w+\]?""".toRegex()

    val addresses = lines.map { line ->
        val (hyper, standard) = pattern.findAll(line).map { it.value }.partition { it.firstOrNull() == '[' }
        IPv7(standard, hyper.map { it.drop(1).dropLast(1) })
    }

    fun part1() = addresses.count { it.supportsTls }

    fun part2() = addresses.count { it.supportsSsl }

}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D7.part1()} (${elapsedTime(time)}ms)") // 118
    time = System.nanoTime()
    println("Part 2: ${Y2015D7.part2()} (${elapsedTime(time)}ms)") // 260
}