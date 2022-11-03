package org.gristle.adventOfCode.y2021.d3

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.stripCarriageReturns
import org.gristle.adventOfCode.utilities.toGrid

class Y2021D3(input: String) {

    private val strippedInput = input.stripCarriageReturns()
    fun part1(): Int {

        fun List<List<Char>>.toInt(matchDigit: Int): Int {
            return this
                .map { freqDist ->
                    freqDist.count { it == matchDigit.digitToChar() } * 2 >= freqDist.size
                }.foldIndexed(0) { index, acc, i ->
                    if (i) {
                        acc + 1.shl(size - 1 - index)
                    } else {
                        acc
                    }
                }
        }

        val cols = strippedInput
            .toGrid()
            .rotate90()
            .flipY()
            .rows()

        val gamma = cols.toInt(1)

        val epsilon = cols.toInt(0)

        return gamma * epsilon
    }

    fun part2(): Int {
        val rows = strippedInput.split('\n')

        var o2Gen = rows
        var pos = 0
        while (o2Gen.size > 1) {
            val shouldBe1 = o2Gen.count { it[pos] == '1' } * 2 >= o2Gen.size
            o2Gen = o2Gen.filter { (shouldBe1 && it[pos] == '1') || (!shouldBe1 && it[pos] == '0') }
            pos++
        }

        var co2 = rows
        pos = 0
        while (co2.size > 1) {
            val shouldBe0 = co2.count { it[pos] == '0' } * 2 <= co2.size
            co2 = co2.filter { (shouldBe0 && it[pos] == '0') || (!shouldBe0 && it[pos] == '1') }
            pos++
        }
        return o2Gen.first().toInt(2) * co2.first().toInt(2)
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2021D3(readRawInput("y2021/d3"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 3969000
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 4267809
}