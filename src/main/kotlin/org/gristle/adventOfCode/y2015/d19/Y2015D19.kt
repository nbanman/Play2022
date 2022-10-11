package org.gristle.adventOfCode.y2015.d19

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D19(private val input: String) {
    data class Rule(val element: String, val replacement: String) {
        val size = replacement.count { it in 'A'..'Z' }
    }

    private val moleculePattern = """^[A-z]+${'$'}""".toRegex(RegexOption.MULTILINE)
    private val molecule = moleculePattern.find(input)?.value
        ?: throw Exception("No molecule found in input string")

    private val rulePattern = """([A-Z][a-z]?) => (\w+)""".toRegex()
    private val rules = input
        .groupValues(rulePattern)
        .map { Rule(it[0], it[1]) }
        .sortedByDescending { it.size }

    fun part1(): Int {
        val elements = """[A-Z][a-z]?"""
            .toRegex()
            .findAll(molecule)
            .toList()

        return elements
            .flatMap { mr ->
                rules
                    .filter { it.element == mr.value }
                    .map { rule ->
                        molecule.substring(0, mr.range.first) + rule.replacement + molecule.substring(mr.range.last + 1)
                    }
            }.distinct()
            .size
    }

    fun part2(): Int {
        val elementPattern = """e => (\w+)""".toRegex()
        val starts = input
            .groupValues(elementPattern)
            .map { it[0] }

        return generateSequence(molecule) { molecule ->
            rules
                .first { rule -> molecule.contains(rule.replacement) } // find first applicable rule
                .let { rule -> molecule.replaceFirst(rule.replacement, rule.element) } // apply rule to molecule; yield
        }.indexOfFirst { starts.contains(it) } + 1
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2015D19(readRawInput("y2015/d19"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 535
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 212
}