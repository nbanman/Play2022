package org.gristle.adventOfCode.y2017.d25

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.groupValues
import org.gristle.adventOfCode.utilities.readRawInput

class Y2017D25(input: String) {
    private class Node(var isOne: Boolean = false, var left: Node? = null, var right: Node? = null) {
        fun value() = if (isOne) 1 else 0

        fun moveLeft(): Node {
            left = left ?: Node(false, null, this)
            return left as Node
        }

        fun moveRight(): Node {
            right = right ?: Node(false, this, null)
            return right as Node
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

    private data class State(
        val zeroWrite: Boolean,
        val zeroLeft: Boolean,
        val zeroChange: String,
        val oneWrite: Boolean,
        val oneLeft: Boolean,
        val oneChange: String
    )

    private val stepPattern = """(\d+) steps""".toRegex()

    private val pattern = """In state ([A-F]):
  If the current value is 0:
    - Write the value (0|1).
    - Move one slot to the (left|right).
    - Continue with state ([A-F]).
  If the current value is 1:
    - Write the value (0|1).
    - Move one slot to the (left|right).
    - Continue with state ([A-F])."""

    private val data = input.replace("\r", "")

    fun part1(): Int {
        val steps = data.groupValues(stepPattern)[0][0].toInt()
        val states = data
            .groupValues(pattern)
            .map {
                it[0] to State(
                    it[1] == "1",
                    it[2] == "left",
                    it[3],
                    it[4] == "1",
                    it[5] == "left",
                    it[6]
                )
            }.let {
                mapOf(*it.toTypedArray())
            }
        val startNode = Node()
        var currentNode = startNode
        var currentName = "A"
        for (x in 1..steps) {
            val state = states.getValue(currentName)
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
    var time = System.nanoTime()
    val c = Y2017D25(readRawInput("y2017/d25"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 3745
}