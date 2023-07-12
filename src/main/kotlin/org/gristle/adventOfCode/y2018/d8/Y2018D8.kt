package org.gristle.adventOfCode.y2018.d8

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getInts

class Y2018D8(input: String) : Day {

    // Node contains child nodes and metadata.
    data class Node(val children: List<Node>, val metadata: List<Int>) {

        private val localMetadataSum = metadata.sum()

        // Using lazy vals instead of fun so that each node only needs to make these calculations once.
        val totalMetadata: Int by lazy { localMetadataSum + children.sumOf(Node::totalMetadata) }

        val value: Int by lazy {
            if (children.isEmpty()) {
                localMetadataSum
            } else {
                metadata.sumOf { index -> children.getOrNull(index - 1)?.value ?: 0 }
            }
        }

        companion object {

            // creates a node given a iterator that supplies numbers. Child nodes are created recursively.
            fun of(numbers: Iterator<Int>): Node {
                val childQuantity = numbers.next()
                val metadataQuantity = numbers.next()
                val children = List(childQuantity) { of(numbers) }
                val metadata = List(metadataQuantity) { numbers.next() }
                return Node(children, metadata)
            }
        }
    }

    // create tree
    private val parent = Node.of(input.getInts().iterator())

    override fun part1() = parent.totalMetadata

    override fun part2() = parent.value
}

fun main() = Day.runDay(Y2018D8::class)

//    Class creation: 15ms
//    Part 1: 36027 (1ms)
//    Part 2: 23960 (0ms)
//    Total time: 17ms
