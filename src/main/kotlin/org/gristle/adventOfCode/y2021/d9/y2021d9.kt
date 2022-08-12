package org.gristle.adventOfCode.y2021.d9

import org.gristle.adventOfCode.utilities.*

object Y2021D9 {
    private val input = readRawInput("y2021/d9")

    private val heightMap = input
        .toGrid()
        .let { charGrid ->
            charGrid.map { Character.getNumericValue(it) }.toGrid(charGrid.width)
        }

    private val lowIndices = heightMap
        .mapIndexedNotNull { index, height ->
            if (heightMap.getNeighbors(index).all { it > height }) {
                index
            } else {
                null
            }
        }

    private fun Int.basinSize(): Int {
        var count = 0
        val heap = Heap.minHeap<Int, Int>()
        heap.add(heightMap[this], this)
        val visited = mutableSetOf<Int>()
        while (true) {
            val x = heap.pollUntil { !visited.contains(it) } ?: return count
            visited.add(x)
            count++
            val higherNeighbors = heightMap
                .getNeighborIndices(x)
                .filter { heightMap[it] != 9 && heightMap[it] > heightMap[x] }
                .map { heightMap[it] to it }
            heap.addAll(higherNeighbors)
        }
    }

    fun part1() = lowIndices.sumOf { heightMap[it] + 1 }

    fun part2() = lowIndices
        .map { it.basinSize() }
        .sortedDescending()
        .take(3)
        .reduce { acc, i -> acc * i }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D9.part1()} (${elapsedTime(time)}ms)") // 448
    time = System.nanoTime()
    println("Part 2: ${Y2021D9.part2()} (${elapsedTime(time)}ms)") // 1417248
}