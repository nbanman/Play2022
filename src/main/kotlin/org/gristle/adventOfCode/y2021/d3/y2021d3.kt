package org.gristle.adventOfCode.y2021.d3

import org.gristle.adventOfCode.utilities.*

object Y2021D3 {
    private val input = readStrippedInput("y2021/d3")

    fun part1(): Long {

        fun List<Boolean>.toLong(): Long {
            return foldIndexed(0L) { index, acc, i ->
                if (i) {
                    acc + 1L.shl(size - 1 - index)
                } else {
                    acc
                }
            }
        }

        val cols = input
            .toGrid()
            .rotate90()
            .flipY()
            .rows()

        val gamma = cols
            .map { freqDist -> freqDist.count { it == '1' } * 2 >= freqDist.size }
            .toLong()

        val epsilon = cols
            .map { freqDist -> freqDist.count { it == '0' } * 2 >= freqDist.size }
            .toLong()


        return gamma * epsilon
    }

    fun part2(): Int {
        val rows = input.split('\n')

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
    println("Part 1: ${Y2021D3.part1()} (${elapsedTime(time)}ms)") // 3969000
    time = System.nanoTime()
    println("Part 2: ${Y2021D3.part2()} (${elapsedTime(time)}ms)") // 4267809
}