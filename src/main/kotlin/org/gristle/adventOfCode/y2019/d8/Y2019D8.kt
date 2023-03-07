package org.gristle.adventOfCode.y2019.d8

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Grid
import org.gristle.adventOfCode.utilities.ocr

private typealias Layer = String

class Y2019D8(input: String) : Day {
    private val width = 25
    private val height = 6

    private val layers = input.chunked(width * height) // Split the data into a list of equal sized layers

    override fun part1() = layers
        .minByOrNull { layer -> layer.count { it == '0' } }  // Get the layer with the fewest 0s
        ?.let { layer -> layer.count { it == '1' } * layer.count { it == '2' } } // Get product of # of 1s and 2s
        ?: throw IllegalArgumentException("'layers' has no elements")

    override fun part2(): String {
        // Takes the layers and an index value representing a specific pixel. For that index, it looks
        // through the layers sequentially and finds the first non-transparent pixel (i.e., it ignores '0's).
        // Then it returns true for white pixels ('1') and false for black pixels ('0').
        fun List<Layer>.visiblePixel(index: Int): Boolean = '1' == first { it[index] != '2' }[index]

        // Make a Grid representing the visible pixels of the image. Then run OCR to turn the grid into a
        // string of letters.
        return Grid(width, height) { i -> layers.visiblePixel(i) }.ocr()
    }
}

fun main() = Day.runDay(Y2019D8::class)

//    Class creation: 25ms
//    Part 1: 1088 (1ms)
//    Part 2: LGYHB (8ms)
//    Total time: 34ms