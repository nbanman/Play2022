package org.gristle.adventOfCode.y2017.d10

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.shift

class Y2017D10(private val input: String) {

    companion object {
        fun denseHash(
            p2Lengths: List<Int>
        ): String {
            val ring = List (256) { it }
            val shiftSum = p2Lengths.sum() * 64
            val skipSum = (p2Lengths.size) * 64
            val p2 = (0 until 64).fold(ring) { acc, i ->
                acc.knotHash(p2Lengths, i * (p2Lengths.size))
            }
            val totalSkips = (1 until skipSum).reduce { acc, i -> acc + i }
            val reshifted = p2.shift(0 - (shiftSum + totalSkips))

            val denseHash = reshifted
                .chunked(16)
                .joinToString("") { chunk ->
                    val reduction = chunk.reduce { acc, i -> acc xor i }
                    String.format("%02x", reduction)
                }
            return denseHash
        }

        private fun <E> List<E>.knotHash(lengths: List<Int>, skip: Int = 0): List<E> {
            return lengths.foldIndexed(this) { index, accRing, length ->
                knot(accRing, length, skip + index)
            }
        }

        private fun <E> knot(ring: List<E>, length: Int, skip: Int): List<E> {
            val reversePart = ring.subList(0, length).reversed()
            return (reversePart + ring.drop(reversePart.size)).shift(length + skip)
        }
    }

    fun part1(): Int {
        val lengths = input.split(',').map { it.toInt() }
        val ring = List (256) { it }
        return ring.knotHash(lengths)
            .shift(0 - (lengths.sum() + ((1 until lengths.size).reduce { acc, i -> acc + i })))
            .take(2)
            .run {
                first() * last()
            }
    }

    fun part2() = denseHash(input.map { it.code } + listOf(17, 31, 73, 47, 23))
}

fun main() {
    var time = System.nanoTime()
    val c = Y2017D10(readRawInput("y2017/d10"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 23874
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // e1a65bfb5a5ce396025fab5528c25a87
}