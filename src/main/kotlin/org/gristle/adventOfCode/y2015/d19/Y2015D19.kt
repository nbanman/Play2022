package org.gristle.adventOfCode.y2015.d19

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.blankSplit
import org.gristle.adventOfCode.utilities.groupValues

class Y2015D19(private val input: String) : Day {

    data class Rule(val element: String, val replacement: String) {
        val size = replacement.count { it in 'A'..'Z' }
    }

    private val molecule: String
    private val rules: List<Rule>

    init {
        val (ruleLines, moleculeLine) = input.blankSplit()
        molecule = moleculeLine
        rules = ruleLines
            .lines()
            .map { line ->
                val (element, replacement) = line.split(" => ")
                Rule(element, replacement)
            }.sortedByDescending { it.size }
    }

    override fun part1(): Int {
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

    override fun part2(): Int {
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

fun main() = Day.runDay(Y2015D19::class)

//    Class creation: 22ms
//    Part 1: 535 (6ms)
//    Part 2: 212 (3ms)
//    Total time: 32ms