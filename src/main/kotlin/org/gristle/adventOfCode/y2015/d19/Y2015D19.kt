package org.gristle.adventOfCode.y2015.d19

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2015D19(input: String) {
    data class Rule(val element: String, val replacement: String) {
        val size = replacement.count { it in 'A'..'Z' }
    }

    private val work = input
    private val moleculePattern = """^[A-z]+${'$'}""".toRegex(RegexOption.MULTILINE)
    private val molecule = moleculePattern.find(work)?.value
        ?: throw IllegalStateException("No molecule found in input")

    private val rulePattern = """([A-Z][a-z]?) => (\w+)""".toRegex()
    private val rules = work
        .groupValues(rulePattern)
        .map { Rule(it[0], it[1]) }
        .sortedByDescending { it.size }

    private val ePattern = """e => (\w+)""".toRegex()
    private val starts = work
        .groupValues(ePattern)
        .map { it[0] }

    private val elements = """[A-Z][a-z]?"""
        .toRegex()
        .findAll(molecule)
        .toList()

    private val part1Mutations = elements.flatMap { mr ->
        rules
            .filter { it.element == mr.value }
            .map { rule ->
                molecule.substring(0, mr.range.first) + rule.replacement + molecule.substring(mr.range.last + 1)
            }
    }.distinct()

    fun part1() = part1Mutations.size

    fun part2() = generateSequence(molecule) {
        var next = ""
        for (rule in rules) {
            if (it.contains(rule.replacement)) {
                next = it.replaceFirst(rule.replacement, rule.element)
                break
            }
        }
        next
    }.indexOfFirst { starts.contains(it) } + 1
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