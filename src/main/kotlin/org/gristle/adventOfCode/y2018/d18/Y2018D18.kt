package org.gristle.adventOfCode.y2018.d18

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.md5
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

// not refactored, very impure
class Y2018D18(input: String) {

    private val collectionArea = input.toGrid()
    
    fun part1(): Int {
        val p1Minutes = 10
        val newCollectionArea = (1..p1Minutes).fold(collectionArea) { acc, _ ->
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
        (1..p2Minutes).fold(collectionArea) { acc, _ ->
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
    val c = Y2018D18(readRawInput("y2018/d18"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 605154
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 200364
}