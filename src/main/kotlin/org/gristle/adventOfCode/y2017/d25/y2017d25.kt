package org.gristle.adventOfCode.y2017.d25

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

object Y2017D25 {
    class Node1725(var isOne: Boolean = false, var left: Node1725? = null, var right: Node1725? = null) {
        fun value() = if (isOne) 1 else 0

        fun moveLeft(): Node1725 {
            left = left ?: Node1725(false, null, this)
            return left as Node1725
        }
        fun moveRight(): Node1725 {
            right = right ?: Node1725(false, this, null)
            return right as Node1725
        }
        fun sumList(): Int {
            return (sumLeft() + sumRight() - value())
        }

        private fun sumLeft(): Int {
            return value() + (left?.sumLeft() ?: 0)
        }

        private fun sumRight(): Int {
            return value() + (right?.sumRight() ?: 0)
        }
    }


    data class State1725(
        val zeroWrite: Boolean,
        val zeroLeft: Boolean,
        val zeroChange: String,
        val oneWrite: Boolean,
        val oneLeft: Boolean,
        val oneChange: String
    )

    private val stepPattern = """(\d+) steps""".toRegex()

    private const val pattern = """In state ([A-F]):
  If the current value is 0:
    - Write the value (0|1).
    - Move one slot to the (left|right).
    - Continue with state ([A-F]).
  If the current value is 1:
    - Write the value (0|1).
    - Move one slot to the (left|right).
    - Continue with state ([A-F])."""

    private val data = readRawInput("y2017/d25").replace("\r", "")

    fun part1(): Int {
        val steps = data.groupValues(stepPattern)[0][0].toInt()
        val states = data
            .groupValues(pattern)
            .map { it[0] to State1725(
                it[1] == "1",
                it[2] == "left",
                it[3],
                it[4] == "1",
                it[5] == "left",
                it[6]
            ) }.let {
                mapOf(*it.toTypedArray())
            }
        val startNode = Node1725()
        var currentNode = startNode
        var currentName = "A"
        for (x in 1..steps) {
            val state = states[currentName]!!
            if (currentNode.isOne) {
                currentNode.isOne = state.oneWrite
                currentNode = if (state.oneLeft) currentNode.moveLeft() else currentNode.moveRight()
                currentName = state.oneChange
            } else {
                currentNode.isOne = state.zeroWrite
                currentNode = if (state.zeroLeft) currentNode.moveLeft() else currentNode.moveRight()
                currentName = state.zeroChange
            }
        }
        return startNode.sumList()
    }

}

fun main() {
    val time = System.nanoTime()
    println("Part 1: ${Y2017D25.part1()} (${elapsedTime(time)}ms)") // 3745
}