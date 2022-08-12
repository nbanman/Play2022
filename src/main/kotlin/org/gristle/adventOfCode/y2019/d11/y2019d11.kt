package org.gristle.adventOfCode.y2019.d11

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

object Y2019D11 {
    private val input = readRawInput("y2019/d11")

    val initialData = input.split(',').map { it.toLong() }

    enum class Paint { BLACK, WHITE }

    data class Panel(val paint: Paint = Paint.BLACK, val numPainted: Int = 0)

    data class Robot(
        val grid: MutableMap<Coord, Panel>,
        var coord: Coord,
        var direction: Nsew,
        val input: Deque<Long>,
        val output: Deque<Long>
    ) {
        fun sendInstruction() {
            val instruction = if (grid[coord]?.paint == Paint.WHITE) 1L else 0L
            output.add(instruction)
        }

        fun run() {
            if (input.size > 1) {
                val panel = grid[coord] ?: Panel()
                val newPaint = if (input.poll() == 0L) Paint.BLACK else Paint.WHITE
                grid[coord] = panel.copy(paint = newPaint, numPainted = panel.numPainted + 1)
                direction = if (input.poll() == 0L) direction.left() else direction.right()
                coord = direction.forward(coord)
                sendInstruction()
            }
        }
    }

    fun part1(): Int {
        val grid = mutableMapOf<Coord, Panel>()
        val fromRobot = LinkedList<Long>()
        val fromComputer = LinkedList<Long>()
        val intCode = IntCode("A", initialData, null, fromRobot, fromComputer)
        val robot = Robot(grid, Coord(0, 0), Nsew.NORTH, fromComputer, fromRobot)
        robot.sendInstruction()
        while (!intCode.isDone) {
            intCode.run()
            robot.run()
        }
        return grid.values.count { it.numPainted >= 1 }
    } 

    fun part2(): String {
        val grid = mutableMapOf<Coord, Panel>()
        grid[Coord(0, 0)] = Panel(Paint.WHITE)
        val fromRobot = LinkedList<Long>()
        val fromComputer = LinkedList<Long>()
        val intCode = IntCode("B", initialData, null, fromRobot, fromComputer)
        val robot = Robot(grid, Coord(0, 0), Nsew.NORTH, fromComputer, fromRobot)
        robot.sendInstruction()
        while (!intCode.isDone) {
            intCode.run()
            robot.run()
        }
        val minX = grid.keys.minOf { it.x }
        val maxX = grid.keys.maxOf { it.x }
        val minY = grid.keys.minOf { it.y }
        val maxY = grid.keys.maxOf { it.y }

        val width = maxX - minX + 1
        val height = maxY - minY + 1

        val printGrid = List(width * height) { i->
            if (grid[Coord(i % width + minX, i / width + minY)]?.paint == Paint.WHITE) '*' else ' '
        }.toGrid(width)
        return printGrid.representation { it }
    } 
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2019D11.part1()} (${elapsedTime(time)}ms)") // 2720
    time = System.nanoTime()
    println("Part 2: \n${Y2019D11.part2()}(${elapsedTime(time)}ms)") // JZPJRAGJ
}