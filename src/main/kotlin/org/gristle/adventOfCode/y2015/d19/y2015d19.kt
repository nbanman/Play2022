package org.gristle.adventOfCode.y2015.d19

import org.gristle.adventOfCode.utilities.*

object Y2015D19 {
    data class Rule(val element: String, val replacement: String) {
        val size = replacement.count { it in 'A'..'Z' }
    }

    private val input = readRawInput("y2015/d19")

    val work = input
    val moleculePattern = """^[A-z]+${'$'}""".toRegex(RegexOption.MULTILINE)
    val molecule = moleculePattern.find(work)!!.value

    val rulePattern = """([A-Z][a-z]?) =\> (\w+)"""
    val rules = work
        .groupValues(rulePattern)
        .map { Rule(it[0], it[1]) }
        .sortedByDescending { it.size }

    val ePattern = """e =\> (\w+)"""
    val starts = work
        .groupValues(ePattern)
        .map { it[0] }

    val elements = """[A-Z][a-z]?"""
        .toRegex()
        .findAll(molecule)
        .toList()

    val part1Mutations = elements.flatMap { mr ->
        rules
            .filter { it.element == mr.value }
            .map { rule ->
                molecule.substring(0, mr.range.first) + rule.replacement + molecule.substring(mr.range.last + 1)
            }
    }.distinct()

    fun part1() = part1Mutations.size

    fun part2() = generateSequence(molecule) {
        var next: String = ""
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
    println("Part 1: ${Y2015D19.part1()} (${elapsedTime(time)}ms)") // 535
    time = System.nanoTime()
    println("Part 2: ${Y2015D19.part2()} (${elapsedTime(time)}ms)") // 212
}