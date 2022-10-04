package org.gristle.adventOfCode.y2016.d9

import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

class Y2016D9(private val input: String) {

    data class Marker(val length: Int, val times: Int, val range: IntRange)

    private fun nextMarker(stream: String): Marker? {
        return """\((\d+)x(\d+)\)"""
            .toRegex()
            .find(stream)
            ?.let {
                Marker(it.groupValues[1].toInt(), it.groupValues[2].toInt(), it.range)
            }
    }

    private fun p1Decompress(): Int {

        val potentialMarkers = """\((\d+)x(\d+)\)"""
            .toRegex()
            .findAll(input)
            .toList()
            .map { Marker(it.groupValues[1].toInt(), it.groupValues[2].toInt(), it.range) }

        var lastMarker = 0
        val markers = potentialMarkers.filter { marker ->
            if (marker.range.first < lastMarker) {
                false
            } else {
                lastMarker = marker.range.last + marker.length
                true
            }
        }

        if (markers.isEmpty()) return input.length

        var parser = 0
        val sb = StringBuilder()

        for (marker in markers) {
            sb.append(input.substring(parser, marker.range.first))
            val repeatRange = input.substring(marker.range.last + 1, marker.range.last + 1 + marker.length)
            sb.append(repeatRange.repeat(marker.times))
            parser = marker.range.last + 1 + marker.length
        }
        sb.append(input.drop(markers.last().range.last + markers.last().length + 1))
        return sb.toString().length
    }

    private fun p2Decompress(s: String): Long {
        var stream = s
        var count = 0L
        while (stream.isNotEmpty()) {
            val nextMarker = nextMarker(stream) ?: return count + stream.length
            count += nextMarker.range.first
            val subStream = stream.substring(nextMarker.range.last + 1, nextMarker.range.last + 1 + nextMarker.length)
            count += nextMarker.times * p2Decompress(subStream)
            stream = stream.drop(nextMarker.range.last + subStream.length + 1) // add + 1?
        }
        return count
    }

    fun part1() = p1Decompress()

    fun part2() = p2Decompress(input)
}

fun main() {
    var time = System.nanoTime()
    val c = Y2016D9(readRawInput("y2016/d9"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 110346
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 10774309173
}