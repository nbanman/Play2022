package org.gristle.adventOfCode.y2019.d17

import org.gristle.adventOfCode.utilities.*
import org.gristle.adventOfCode.y2019.IntCode.IntCode
import java.util.*

object Y2019D17 {
    private val input = readRawInput("y2019/d17")
    private val initialState = input.split(',').map { it.toLong() }
    
    fun solve(): Pair<Int, Long> {

        val toDroid: Deque<Long> = LinkedList()
        val toComp: Deque<Long> = LinkedList()
        val intCode = IntCode("A", initialState, null, toComp, toDroid)
        intCode.run()
        val width = toDroid.indexOfFirst { it == 10L }
        val grid = toDroid.mapNotNull { if (it != null && it != 10L) it else null }.toMutableGrid(width)
        val intersections = grid.mapIndexedNotNull { index, l ->
            if (l != 35L || grid.getNeighbors(index).any { it != 35L }) {
                null
            } else {
                grid.coordOf(index)
            }
        }.map { it.x * it.y}
        val p1 = intersections.sum()

        // Part 2
        var coord = grid.coordOfElement(94L)
        var dir = Nsew.NORTH
        var counter = 0
        val path = mutableListOf<String>()
        while (true) {
            if (cromulent(grid, coord, dir)) {
                counter++
                coord = dir.forward(coord)
            } else {
                if (cromulent(grid, coord, dir.left())) {
                    dir = dir.left()
                    if (counter != 0) path.add(counter.toString())
                    path.add("L")
                    counter = 0
                } else if (cromulent(grid, coord, dir.right())) {
                    dir = dir.right()
                    if (counter != 0) path.add(counter.toString())
                    path.add("R")
                    counter = 0
                } else {
                    path.add(counter.toString())
                    break
                }
            }
        }
        val formSeq = listOf('A', ',', 'B', ',', 'A', ',', 'C', ',', 'A', ',', 'A', ',', 'C', ',', 'B', ',', 'C', ',', 'B', '\n')
        val aForm = listOf('L', ',', '1', '2', ',', 'L', ',', '8', ',', 'R', ',', '1', '2', '\n')
        val bForm = listOf('L', ',', '1', '0', ',', 'L', ',', '8', ',', 'L', ',', '1', '2', ',', 'R', ',', '1', '2', '\n')
        val cForm = listOf('R', ',', '1', '2', ',', 'L', ',', '8', ',', 'L', ',', '1', '0', '\n', 'n', '\n')
        val commands = (formSeq + aForm + bForm + cForm).map { it.code.toLong() }
        toComp.addAll(commands)
        val intCodeB = IntCode("B", listOf(2L) + initialState.drop(1), null, toComp, toDroid)
        toDroid.clear()
        intCodeB.run()
        return p1 to toDroid.last()
    }

    private fun cromulent(grid: Grid<Long>, coord: Coord, dir: Nsew): Boolean {
        val prospect = dir.forward(coord)
        return (prospect.x in 0 until grid.width && prospect.y in 0 until grid.height) && grid[prospect] == 35L
    }
}

fun main() {
    val time = System.nanoTime()
    val (p1, p2) = Y2019D17.solve()
    println("Part 1: $p1") // 10632
    println("Part 2: $p2 (${elapsedTime(time)}ms)") // 1356191
}