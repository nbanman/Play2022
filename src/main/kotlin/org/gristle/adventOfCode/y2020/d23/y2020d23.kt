package org.gristle.adventOfCode.y2020.d23

import org.gristle.adventOfCode.utilities.IndexedLinkedList
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.shift

object Y2020D23 {
    private val input = readRawInput("y2020/d23")

    fun IndexedLinkedList<Int>.move(moves: Int, largest: Int) {
        var current = header
        (1..moves).forEach { _ ->
            val cut = current.cut(3)
            val cutValues = listOf(cut.value, cut.next!!.value, cut.next!!.next!!.value)
            val pastePos = let {
                var nextValue = (current.value - 1).let { if (it > 0) it else largest }
                while (nextValue in cutValues) {
                    nextValue = (nextValue - 1).let { if (it > 0) it else largest }
                }
                index[nextValue]!!
            }
            pastePos.add(cut)
            current = current.next!!
        }
    }

    fun part1(): String {
        val moves = 100
        val elements = input.map { Character.getNumericValue(it) }
        val circle = IndexedLinkedList(elements, true)
        circle.move(moves, elements.maxOrNull()!!)
        return circle
            .toList()
            .let {
                it.shift(it.indexOf(1))
            }.drop(1)
            .joinToString("")
    }

    fun part2(): Long {
        val moves = 10_000_000
        val size = 1_000_000
        val elements = List(size) { i ->
            if (i in input.indices) Character.getNumericValue(input[i]) else i + 1
        }
        val circle = IndexedLinkedList(elements, true)
        circle.move(moves, size)
        val circleList = circle.toList()
        val indexOf1 = circleList.indexOf(1)
        return circleList[indexOf1 + 1].toLong() * circleList[indexOf1 + 2]
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2020D23.part1()} (${elapsedTime(time)}ms)") // 94238657
    time = System.nanoTime()
    println("Part 2: ${Y2020D23.part2()} (${elapsedTime(time)}ms)") // 3072905352
}