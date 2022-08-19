package org.gristle.adventOfCode.y2015.d16

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

object Y2015D16 {
    private val input = readRawInput("y2015/d16")

    data class Sue(
        val number: Int,
        val children: Int,
        val cats: Int,
        val samoyeds: Int,
        val pomeranians: Int,
        val akitas: Int,
        val vizslas: Int,
        val goldfish: Int,
        val trees: Int,
        val cars: Int,
        val perfumes: Int,
    ) {
        private fun compatible(code: String, amount: Int, part2: Boolean = false): Boolean {
            return when (code) {
                "children" -> children == -1 || children == amount
                "cats" -> cats == -1 || (if (part2) cats > amount else cats == amount)
                "samoyeds" -> samoyeds == -1 || samoyeds == amount
                "pomeranians" -> pomeranians == -1 || (if (part2) pomeranians < amount else pomeranians == amount)
                "akitas" -> akitas == -1 || akitas == amount
                "vizslas" -> vizslas == -1 || vizslas == amount
                "goldfish" -> goldfish == -1 || (if (part2) goldfish < amount else goldfish == amount)
                "trees" -> trees == -1 || (if (part2) trees > amount else trees == amount)
                "cars" -> cars == -1 || cars == amount
                "perfumes" -> perfumes == -1 || perfumes == amount
                else -> true
            }
        }

        fun compatible(criteria: List<Pair<String, Int>>, part2: Boolean = false): Boolean {
            for (criterion in criteria) {
                if (!compatible(criterion.first, criterion.second, part2)) return false
            }
            return true
        }
    }

    class SueBuilder {
        private var number = -1
        private var children = -1
        private var cats = -1
        private var samoyeds = -1
        private var pomeranians = -1
        private var akitas = -1
        private var vizslas = -1
        private var goldfish = -1
        private var trees = -1
        private var cars = -1
        private var perfumes = -1

        fun build() =
            Sue(number, children, cats, samoyeds, pomeranians, akitas, vizslas, goldfish, trees, cars, perfumes)

        fun add(code: String, amount: Int) {
            when (code) {
                "Sue" -> number = amount
                "children" -> children = amount
                "cats" -> cats = amount
                "samoyeds" -> samoyeds = amount
                "pomeranians" -> pomeranians = amount
                "akitas" -> akitas = amount
                "vizslas" -> vizslas = amount
                "goldfish" -> goldfish = amount
                "trees" -> trees = amount
                "cars" -> cars = amount
                "perfumes" -> perfumes = amount
            }
        }
    }

    private const val pattern = """(Sue) (\d+): (\w+): (\d+), (\w+): (\d+), (\w+): (\d+)"""

    private val sues = input
        .groupValues(pattern)
        .map { gv ->
            SueBuilder()
                .apply {
                    add(gv[0], gv[1].toInt())
                    add(gv[2], gv[3].toInt())
                    add(gv[4], gv[5].toInt())
                    add(gv[6], gv[7].toInt())
                }.build()
        }

    private const val tickerTape = "children: 3\n" +
            "cats: 7\n" +
            "samoyeds: 2\n" +
            "pomeranians: 3\n" +
            "akitas: 0\n" +
            "vizslas: 0\n" +
            "goldfish: 5\n" +
            "trees: 3\n" +
            "cars: 2\n" +
            "perfumes: 1"

    private val criteria = tickerTape
        .groupValues("""(\w+): (\d+)""")
        .map { gv -> gv[0] to gv[1].toInt() }

    fun part1() = sues.find { it.compatible(criteria) }?.number

    fun part2() = sues.find { it.compatible(criteria, true) }?.number
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2015D16.part1()} (${elapsedTime(time)}ms)") // 40
    time = System.nanoTime()
    println("Part 2: ${Y2015D16.part2()} (${elapsedTime(time)}ms)") // 241
}