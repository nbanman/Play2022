package org.gristle.adventOfCode.y2022.d20

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList

class Y2022D20(input: String) : Day {

    class Node(
        val value: Long,
        prev: Node? = null,
        next: Node? = null,
    ) {
        var prev: Node = prev
            ?.also { it.next = this }
            ?: this
        var next: Node = next
            ?.also { it.prev = this }
            ?: this

        fun move(size: Int) {
            val steps = value.mod(size - 1)

            if (steps == 0) return

            val moveNode = if (steps > size / 2) {
                generateSequence(this, Node::prev).take(size - steps + 1).last()
            } else {
                val actual = generateSequence(this, Node::next).take(steps + 1).last()
                actual
            }

            // take node out
            prev.next = next
            next.prev = prev

            // fix node pointers
            next = moveNode.next
            prev = moveNode


            // add node
            moveNode.next.prev = this
            moveNode.next = this
        }

        override fun toString(): String {
            return "Node(value=$value, prev=${prev.value}, next=${next.value})"
        }
    }

    private val initialValues = input.getIntList()

    fun solve(factor: Long, times: Int): Long {
        val values: List<Long> = initialValues.map { it * factor }

        val nodes = buildList {
            val header = Node(values.first())
            add(header)
            var previous = header
            for (n in values.drop(1)) {
                previous = Node(n, previous, header)
                add(previous)
            }
        }

        val zeroNode = nodes.find { it.value == 0L } ?: throw IllegalStateException("0 not found in nodes")
        repeat(times) {
            for (node in nodes) {
                node.move(nodes.size)
            }
        }

        val traverse = generateSequence(zeroNode, Node::next).drop(1)

        return (1..3).fold(0L) { acc, thousand ->
            acc + traverse.take((thousand * 1000) % nodes.size).last().value
        }
    }

    override fun part1() = solve(1, 1)

    override fun part2() = solve(811589153, 10)
}

fun main() = Day.runDay(Y2022D20::class)

//    Class creation: 10ms
//    Part 1: 4151 (60ms)
//    Part 2: 7848878698663 (314ms)
//    Total time: 385ms