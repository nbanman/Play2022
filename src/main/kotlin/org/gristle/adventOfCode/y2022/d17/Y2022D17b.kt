package org.gristle.adventOfCode.y2022.d17

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Nsew
import org.gristle.adventOfCode.utilities.getInts

class Y2022D17b(private val jetPattern: String) : Day {

    private val chamberSize = 256
    private val chamberWidth = 7
    private val rockOffset = 3

    private fun translate(s: String): Long = s.getInts().chunked(2) { (x, y) -> 1 shl (y * 7 + x) }.sum().toLong()

    private val rocks: List<Long> =
        listOf(
            "2 0 3 0 4 0 5 0",
            "3 0 2 1 2 2 3 2 3 3",
            "2 0 3 0 4 0 4 1 4 2",
            "2 0 2 1 2 2 2 3",
            "2 0 3 0 2 1 3 1"
        ).map(::translate)

    private val rocks2: List<List<Long>> = listOf(
        listOf(60),
        listOf(8, 28, 8),
        listOf(28, 16, 16),
        listOf(4, 4, 4, 4),
        listOf(12, 12),
    )

    private val jetstream: Sequence<Nsew> = sequence {
        val directions = jetPattern.map(Nsew::of)
        while (true) {
            directions.forEach { yield(it) }
        }
    }

    private val rockStream: Sequence<Long> = generateSequence(::rocks).flatten()

    // Block-specific functions:

    private fun List<Long>.moveLeft(): List<Long>? =
        if (any { it and 1L == 1L }) {
            null
        } else {
            map { it shr 1 }
        }

    private fun Long.moveLeft() =
        if ((0..3).none { offset -> shr(offset * chamberWidth) and 1 == 1L }) {
            this shr 1
        } else {
            null
        }

    private fun Long.moveRight() =
        if ((0..3).none { offset -> shr(offset * chamberWidth + chamberWidth - 1) and 1 == 1L }) {
            this shl 1
        } else {
            null
        }

    private fun List<Long>.moveRight(): List<Long>? =
        if (any { it shr (chamberWidth - 1) and 1L == 1L }) {
            null
        } else {
            map { it shl 1 }
        }

    // Chamber-specific functions:

    // probably don't need, out of date, replace hard #s with values    
    private fun Long.spansChamber(): Boolean {
        return (0..7).all { x ->
            (0..8).any { y -> shr(y * 8 + x) and 1 == 1L }
        }
    }

    private fun ArrayDeque<Long>.toState(): Long {
        val iterator = iterator()
        return (0 until (kotlin.math.min(size, 63 / chamberWidth))).fold(0L) { acc, offset ->
            acc + (iterator.next() shl offset)
        }
    }

    private fun <E> ArrayDeque<E>.addRolling(element: E) {
        while (size >= chamberSize) removeFirst()
        addLast(element)
    }

    private fun <E> ArrayDeque<E>.addRolling(elements: List<E>, lines: Int) {
        repeat(size + elements.size - lines) { removeFirst() }
        addAll(elements)
    }

    // Gameplay functions

    private fun move(
        winds: Iterator<Nsew>,
        currentPos: Long,
        chamberRows: Long
    ): Long {
        when (val wind = winds.next()) {
            Nsew.EAST -> currentPos.moveRight()
                ?: currentPos

            Nsew.WEST -> currentPos.moveLeft()
                ?: currentPos

            else -> throw IllegalArgumentException("$wind is invalid wind direction")
        }

        return 0
    }

    private fun List<Long>.integrateChamberRows(chamberRows: List<Int>) {}

    private fun Long.place(winds: Iterator<Nsew>, chamber: ArrayDeque<Long>, dropSequence: Sequence<Int>): Long {
        var currentPos = this
        // before getting to existing stack, unit moves around
        repeat(rockOffset) { currentPos = move(winds, currentPos, 0L) }

        // standard loop
        dropSequence
            .first { drop ->
                true
            }
        return 0
    }


    private fun solve(numberOfRocks: Long): Long {
        val winds = jetstream.iterator()
        val rocks = rockStream.iterator()
        val chamber = ArrayDeque<Long>(1024)
        val dropSequence: Sequence<Int> = generateSequence(0) { it + 1 }

        // lay down ground
        chamber.addLast(127)

        var height = 0L

        for (i in 1..numberOfRocks) {
            val rock = rocks.next()
            height += rock.place(winds, chamber, dropSequence)
        }

        return 0
    }
    
    override fun part1(): Any? {
        TODO("Not yet implemented")
    }

    override fun part2(): Any? {
        TODO("Not yet implemented")
    }
}

fun main() = Day.runDay(Y2022D17b::class)