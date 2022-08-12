package org.gristle.adventOfCode.y2018.d18

import org.gristle.adventOfCode.utilities.*

// not refactored
object Y2018D18 {
    private val input = readRawInput("y2018/d18")

    private val collectionArea = input.toGrid()
    
    fun part1(): Int {
        val p1Minutes = 10
        val newCollectionArea = (1..p1Minutes).fold(collectionArea) { acc, minute ->
            acc.mapIndexed { index, c ->
                val neighbors = acc.getNeighbors(index, true)
                when (c) {
                    '.' -> {
                        if (neighbors.count { it == '|' } >= 3) '|' else c
                    }
                    '|' -> {
                        if (neighbors.count { it == '#' } >= 3) '#' else c
                    }
                    else -> {
                        if ('#' in neighbors && '|' in neighbors) '#' else '.'
                    }
                }
            }.toGrid(acc.width)
        }
        return newCollectionArea.count { it == '|' } * newCollectionArea.count { it == '#' }
    }

    fun part2(): Int {
        // Part 2
        val p2Minutes = 494
        var hashes = mutableListOf<Pair<String, Int>>()
        var repeatStart = 0
        val newCollectionArea2 = (1..p2Minutes).fold(collectionArea) { acc, minute ->
            val new = acc.mapIndexed { index, c ->
                val neighbors = acc.getNeighbors(index, true)
                when (c) {
                    '.' -> {
                        if (neighbors.count { it == '|' } >= 3) '|' else c
                    }
                    '|' -> {
                        if (neighbors.count { it == '#' } >= 3) '#' else c
                    }
                    else -> {
                        if ('#' in neighbors && '|' in neighbors) '#' else '.'
                    }
                }
            }.toGrid(acc.width)
            val md5 = new.joinToString("").md5() to acc.count { it == '|' } * acc.count { it == '#' }
            if (md5 in hashes) {
                repeatStart = hashes.indexOf(md5)
            } else {
                hashes.add(md5)
            }
            new
        }
        hashes = hashes.drop(repeatStart).toMutableList()
        return hashes[(1000000000 - repeatStart) % hashes.size].second
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2018D18.part1()} (${elapsedTime(time)}ms)") // 605154
    time = System.nanoTime()
    println("Part 2: ${Y2018D18.part2()} (${elapsedTime(time)}ms)") // 200364
}