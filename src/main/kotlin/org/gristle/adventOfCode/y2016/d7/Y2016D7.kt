package org.gristle.adventOfCode.y2016.d7

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues

class Y2016D7(input: String) : Day {
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

    override fun part1() = addresses.count(IPv7::supportsTls)

    override fun part2() = addresses.count(IPv7::supportsSsl)

}

fun main() = Day.runDay(7, 2016, Y2016D7::class)

//    Class creation: 130ms
//    Part 1: 118 (0ms)
//    Part 2: 260 (0ms)
//    Total time: 131ms