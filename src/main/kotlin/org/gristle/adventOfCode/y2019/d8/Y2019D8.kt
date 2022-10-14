package org.gristle.adventOfCode.y2019.d8

import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.ocr
import org.gristle.adventOfCode.utilities.readRawInput

class Y2019D8(input: String) {
    private val width = 25
    private val height = 6

    private val layers = input.chunked(width * height) // Split the data into a list of equal sized layers

    fun part1(): Int? {
        return layers
            .minByOrNull { layer -> layer.count { it == '0' } }  // Get the layer with the fewest 0s
            ?.let { layer -> layer.count { it == '1' } * layer.count { it == '2' } } // Get product of # of 1s and 2s
    }

    fun part2(): String {
        // Takes the layers and an index value representing a specific pixel. For that index, it looks
        // through the layers sequentially and finds the first non-transparent pixel (i.e., it ignores '0's).
        // Then it returns '#' for white pixels ('1') and ' ' for black pixels ('0').
        fun List<String>.visiblePixel(index: Int) = if (first { it[index] != '2' }[index] == '1') '#' else ' '

        // Make a representing the visible pixels of the image. Then run OCR to turn the grid into a
        // string of letters.
        return Grid(width, height) { i -> layers.visiblePixel(i) }.ocr()
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D8(readRawInput("y2019/d8"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 1088
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // LGYHB
}