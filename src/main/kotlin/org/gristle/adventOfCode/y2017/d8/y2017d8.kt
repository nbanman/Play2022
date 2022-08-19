package org.gristle.adventOfCode.y2017.d8

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput
import kotlin.math.max

object Y2017D8 {
    private val input = readRawInput("y2017/d8")

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

    fun solve(): Pair<Int?, Int> {
        val register = mutableMapOf<String, Int>().withDefault { 0 }
        var highest = 0
        instructions.forEach {
            highest = max(highest, it.execute(register))
        }
        return register.values.maxOrNull() to highest
    }}

fun main() {
    val time = System.nanoTime()
    val (p1, p2) = Y2017D8.solve()
    println("Part 1: $p1\nPart 2: $p2 (${elapsedTime(time)}ms)") // 6343, 7184
}