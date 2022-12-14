package org.gristle.adventOfCode.y2018.d8

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2018D8(input: String) {

    private val licence = input.split(' ').map(String::toInt)

    data class Node(val children: List<Node>, val metadata: List<Int>) {
        companion object {
            fun of(numbers: List<Int>): Node = nodeToLength(numbers).first

            private fun nodeToLength(numbers: List<Int>): Pair<Node, Int> {
                val nodes = numbers[0]
                val metadataEntries = numbers[1]
                var parser = 2

                // make nodes
                val children = List(nodes) {
                    val (child, length) = nodeToLength(numbers.drop(parser))
                    parser += length
                    child
                }

                // make metadata
                val metadata = List(metadataEntries) {
                    parser++
                    numbers[parser - 1]
                }

                return Node(children, metadata) to parser
            }
        }

        val sumOfMetadata: Int by lazy {
            metadata.sum() + children.sumOf { it.sumOfMetadata }
        }

        // Using lazy val instead of fun so that each node only needs to make this calculation once.
        val value: Int by lazy {
            // If no children...
            if (children.isEmpty()) {
                // the value is the sum of the metadata entries
                metadata.sum()
            } else { // but if there are children...
                // the metadata entries become indexes for the children, and the value is the sum of the
                // values of the referenced children. Out of range indexes don't count so return 0.
                metadata.sumOf { if (it - 1 !in children.indices) 0 else children[it - 1].value }
            }
        }

    }

    private val licenseTree = Node.of(licence)

    fun part1() = licenseTree.sumOfMetadata

    fun part2() = licenseTree.value
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D8(readRawInput("y2018/d8"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 36027
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 23960
}