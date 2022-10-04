package org.gristle.adventOfCode.y2020.d16

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.transpose

class Y2020D16(input: String) {

    data class Rule(val name: String, val lowRange: IntRange, val hiRange: IntRange) {
        fun validFor(values: Iterable<Int>) = values.all { it in lowRange || it in hiRange }
    }

    private val rules = input
        .groupValues("""([a-z]+(?: [a-z]+)?): (\d+)-(\d+) or (\d+)-(\d+)""")
        .map { gv ->
            val lowRange = gv[1].toInt()..gv[2].toInt()
            val hiRange = gv[3].toInt()..gv[4].toInt()
            Rule(gv[0], lowRange, hiRange)
        }

    private val tickets = Regex("""\d+(?:,\d+)+""")
        .findAll(input)
        .toList()
        .map { result -> result.value.split(',').map { it.toInt() } }

    fun part1(): Int {
        return tickets
            .flatten()
            .filter { value -> rules.all { value !in it.lowRange && value !in it.hiRange } }
            .sum()
    }

    fun part2(): Long {
        val validTickets = tickets
            .filter { ticket ->
                ticket.all { value -> rules.any { value in it.lowRange || value in it.hiRange } }
            }
        val valueList = validTickets.transpose()
        val sorter = valueList
            .mapIndexed { index, values ->
                index to rules.filter { it.validFor(values) }.toMutableList()
            }.toMutableList()
        val register = mutableMapOf<Rule, Int>()
        while (sorter.isNotEmpty()) {
            sorter.filter { it.second.size == 1 }.forEach { singleton ->
                register[singleton.second.first()] = singleton.first
                sorter.remove(singleton)
                sorter.forEach { it.second.remove(singleton.second.first()) }
            }
        }
        return rules
            .filter { it.name.startsWith("departure") }
            .map { validTickets.first()[register[it]!!] }
            .fold (1L) { acc, i -> acc * i }
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2020D16(readRawInput("y2020/d16"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 29878
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 855438643439
}