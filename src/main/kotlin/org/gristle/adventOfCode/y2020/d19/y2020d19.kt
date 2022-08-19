package org.gristle.adventOfCode.y2020.d19

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

object Y2020D19 {
    private val input = readRawInput("y2020/d19")

    sealed class Rule(val name: Int) {

        abstract fun expand(register: Map<Int, Rule>): String

        class Value(name: Int, val value: Char) : Rule(name) {
            override fun expand(register: Map<Int, Rule>) = "$value"

            override fun toString() = "$value: $name"
        }

        class Seq(name: Int, private val subRules: List<Int>) : Rule(name) {
            override fun expand(register: Map<Int, Rule>): String {
                return subRules.joinToString("") { register[it]!!.expand(register) }
            }

            override fun toString() = "Seq: $name, $subRules"
        }

        class Fork(name: Int, val left: List<Int>, val right: List<Int>) : Rule(name) {
            private var counter = 0
            override fun expand(register: Map<Int, Rule>): String {
                if (name == 8 || name == 11) counter++

                return if (counter < 5) {
                    buildString {
                        append('(')
                        append(left.joinToString("") {
                            register[it]!!.expand(register)
                        })
                        append('|')
                        append(right.joinToString("") {
                            register[it]!!.expand(register)
                        })
                        append(')')
                    }
                } else {
                    left.joinToString("") { register[it]!!.expand(register) }
                }
            }

            override fun toString() = "Fork: $name, $left | $right"
        }
    }

    private val rulePattern = """(\d+): (?:("[a-b]")|(\d+(?: \d+)*)(?: \| (\d+(?: \d+)*))?)""".toRegex()
    private val messagePattern = """^[a-b]+""".toRegex(RegexOption.MULTILINE)

    private val rules = input.groupValues(rulePattern).map { gv ->
        val name = gv[0].toInt()
        val letter = gv[1].drop(1).dropLast(1)
        val left = if (gv[2] == "") null else gv[2].split(' ').map { it.toInt() }
        val right = if (gv[3] == "") null else gv[3].split(' ').map { it.toInt() }

        when {
            letter == "a" -> Rule.Value(name, letter[0])
            letter == "b" -> Rule.Value(name, letter[0])
            right == null -> Rule.Seq(name, left!!)
            else -> Rule.Fork(name, left!!, right)
        }
    }

    private val messages = messagePattern
        .findAll(input)
        .toList()
        .map { it.value }
        .toSet()

    fun part1(): Int {
        val register = mutableMapOf<Int, Rule>()
            .apply {
                rules.forEach { rule ->
                    put(rule.name, rule)
                }
            }.toMap()

        val matchPattern = rules.find { it.name == 0 }!!.expand(register).toRegex()

        return messages.count { matchPattern.matches(it) }

    }

    fun part2(): Int {
        val newRules = rules.sortedBy { it.name }.toMutableList()
        val new8 = Rule.Fork(8, listOf(42), listOf(42, 8))
        val new11 = Rule.Fork(11, listOf(42, 31), listOf(42, 11, 31))

        newRules[8] = new8
        newRules[11] = new11


        val register = mutableMapOf<Int, Rule>()
            .apply {
                newRules.forEach { rule ->
                    put(rule.name, rule)
                }
            }.toMap()

        val matchPattern = newRules.find { it.name == 0 }!!.expand(register).toRegex()

        return messages.count { matchPattern.matches(it) }

    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D19.part1()} (${elapsedTime(time)}ms)") // 151
    time = System.nanoTime()
    println("Part 2: ${Y2020D19.part2()} (${elapsedTime(time)}ms)") // 386
}