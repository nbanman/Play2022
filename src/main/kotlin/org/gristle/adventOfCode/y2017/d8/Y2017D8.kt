package org.gristle.adventOfCode.y2017.d8

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.groupValues

class Y2017D8(input: String) : Day {

    private val pattern = """([a-z]+) (inc|dec) (-?\d+) if ([a-z]+) (<=|<|==|!=|>|>=) (-?\d+)""".toRegex()

    data class Instruction(
        val operand: String,
        val amount: Int,
        val conVar: String,
        val conOp: String,
        val conAmt: Int
    ) {

        fun execute(register: MutableMap<String, Int>): Int {
            val conVal = register.getValue(conVar)
            val meetsCondition = when (conOp) {
                "<=" -> conVal <= conAmt
                "<" -> conVal < conAmt
                "==" -> conVal == conAmt
                "!=" -> conVal != conAmt
                ">=" -> conVal >= conAmt
                else -> conVal > conAmt
            }
            if (meetsCondition) {
                register[operand] = register.getValue(operand) + amount
            }
            return register.getValue(operand)
        }
    }

    val instructions = input
        .groupValues(pattern)
        .map { gv ->
            val amount = gv[2].toInt().let { if (gv[1] == "dec") -it else it }
            Instruction(gv[0], amount, gv[3], gv[4], gv[5].toInt())
        }

    private val solution = let {
        val register = mutableMapOf<String, Int>().withDefault { 0 }
        val highest = instructions.maxOf { it.execute(register) }
        register.values.max() to highest
    }

    override fun part1() = solution.first

    override fun part2() = solution.second
}

fun main() = Day.runDay(Y2017D8::class)

//    Class creation: 35ms
//    Part 1: 6343 (0ms)
//    Part 2: 7184 (0ms)
//    Total time: 35ms
