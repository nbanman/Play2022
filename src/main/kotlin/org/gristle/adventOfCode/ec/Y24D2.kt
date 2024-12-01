package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.toGrid
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.min
import kotlin.text.split

fun main() {
    val timer = Stopwatch(true)
    println("1. ${part1()}: ${timer.lap()}ms")
    println("2. ${part2()}: ${timer.lap()}ms")
    println("3. ${part3()}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private fun part1(): Int {
    val (words, code) = input1.split("\n\n")
    return parseWords(words).sumOf { word -> code.windowed(word.length).count { it == word } }
}

private fun part2(): Int {
    val (wordsStr, code) = input2.split("\n\n")
    val words = parseWords(wordsStr).let { half ->
        (half + half.map { it.reversed() })
            .toSet()
    }
    val wordLengths = words.map { it.length }.distinct().sortedDescending()
    val symbols = BooleanArray(code.length)

    for ((index, c) in code.withIndex()) {
        if (c in "\n ") continue
        var snippet = code
            .substring(index, min(code.length, index + wordLengths.first()))
            .takeWhile { it !in "\n " }
        for (length in wordLengths) {
            if (snippet.length < length) continue
            snippet = snippet.take(length)
            if (snippet in words) {
                for (i in index until index + length) symbols[i] = true
                break
            }
        }
    }
    return symbols.count { it }
}

private fun part3(): Int {
    val (wordsStr, code) = input3.split("\n\n")
    val words = parseWords(wordsStr)
        .let { half ->
            buildSet<List<Char>> {
                val halfList = half.map { it.toList() }
                addAll(halfList)
                addAll(halfList.map { it.reversed() })
            }
        }
    val wordLengths = words.map { it.size }.distinct().sortedDescending()

    val armor = code.toGrid()
    val symbols = mutableSetOf<Coord>()
    for (pos in armor.coords()) {
        var (eastIndex, east) = armor.east(pos, wordLengths.first())
        for (length in wordLengths) {
            if (length != east.size) east = east.take(length)
            if (east in words) {
                symbols.addAll(eastIndex.take(length))
                break
            }
        }

        var south = armor.south(pos, wordLengths.first())
        for (length in wordLengths) {
            if (south.size < length) continue
            if (length < south.size) south = south.take(length)
            if (south in words) {
                symbols.addAll(pos.lineTo(pos.copy(y = pos.y + length - 1)))
                break
            }
        }
    }
    return symbols.size
}

private fun parseWords(wordsStr: String): List<String> = wordsStr
    .dropWhile { it != ':' }
    .drop(1)
    .split(',')

private fun Grid<Char>.east(pos: Coord, length: Int): Pair<List<Coord>, List<Char>> {
    val row = row(pos.y)
    val indices = List(length) { i ->
        (pos.x + i) % width
    }
    return indices.map { Coord(it, pos.y) } to indices.map { row[it] }
}

private fun Grid<Char>.south(pos: Coord, length: Int): List<Char> =
    (pos.y until min(height, pos.y + length))
        .map { y -> this[Coord(pos.x, y)] }

private val input1 = ecPart(24, 2, 1)
private val input2 = ecPart(24, 2, 2)
private val input3 = ecPart(24, 2, 3)
