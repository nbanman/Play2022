package org.gristle.adventOfCode.y2019.d13

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

class Y2019D13(input: String) {

    private val initialState = input.split(',').map { it.toLong() }
    
    data class Cabinet(
        val grid: MutableGrid<Int>,
        val input: Deque<Long>,
        val output: Deque<Long>
    ) {
        var score = 0
        private var ballPosition: Coord = Coord(0, 0)
        private var paddlePosition: Coord = Coord(0, 0)
        fun runSimple() {
            while (input.size > 2) {
                val coord = Coord(input.poll().toInt(), input.poll().toInt())
                grid[coord] = input.poll().toInt()
            }
        }

        fun run() {
            while (input.size > 2) {
                val coord = Coord(input.poll().toInt(), input.poll().toInt())
                val block = input.poll().toInt()
                if (coord == Coord(-1, 0)) {
                    score = block
                } else {
                    grid[coord] = block
                    if (block == 3) paddlePosition = coord
                    if (block == 4) ballPosition = coord
                }
            }
            output.add((ballPosition.x - paddlePosition.x).let { if (it < 0) -1L else if (it > 0) 1L else 0L })
        }
    }

    fun part1(): Int {
        val inp: Deque<Long> = LinkedList()
        val output: Deque<Long> = LinkedList()
        val intCode = IntCode("A", initialState, 2L, null, output)
        intCode.run()
        val grid = List(50_000) { 0 }.toMutableGrid(100)
        val cabinet = Cabinet(grid, output, inp)
        cabinet.runSimple()
        return grid.count { it == 2 }
    }

    fun part2(): Int {
        val inp: Deque<Long> = LinkedList()
        val output: Deque<Long> = LinkedList()
        val intCode = IntCode("B", listOf(2L) + initialState.drop(1), null, inp, output)
        val grid = List(50_000) { 0 }.toMutableGrid(100)
        val cabinet = Cabinet(grid, output, inp)
        while (!intCode.isDone) {
            intCode.run()
            cabinet.run()
        }
        return cabinet.score
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2019D13(readRawInput("y2019/d13"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 348
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 16999
}