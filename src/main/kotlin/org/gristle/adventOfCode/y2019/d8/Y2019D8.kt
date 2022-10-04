package org.gristle.adventOfCode.y2019.d8

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.ocr
import org.gristle.adventOfCode.utilities.readRawInput
import org.gristle.adventOfCode.utilities.toGrid

class Y2019D8(input: String) {

    private val layers = input.chunked(25 * 6) // Split the data into a list of equal sized layers
    
    fun part1(): Int? {
        return layers
            .minByOrNull { layer -> layer.count { it == '0' } }  // Get the layer with the fewest 0s
            ?.let { layer -> layer.count { it == '1' } * layer.count { it == '2' } } // Get the product of # of 1s and 2s
    }

    fun part2(): String {
        // Make a list the same size as a layer. In each entry, grab the values in each of the layers at that index and
        // use the first non-transparent value.
        return layers[0]
            .mapIndexed { i, _ -> layers.map { it[i] }.first { it != '2' } == '1' }
            .toGrid(25)
            .ocr()
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