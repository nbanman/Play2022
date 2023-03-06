package org.gristle.adventOfCode.y2018.d11

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord

class Y2018D11(private val input: String) : Day {

    private fun powerLevel(c: Coord): Int {
        val rackId = c.x + 10
        return (((rackId * c.y + input.toInt()) * rackId) % 1000) / 100 - 5
    }

    private data class Maximum(val c: Coord, val power: Int, val size: Int)

    private val grid = List(300) { y ->
        List(300) { x ->
            powerLevel(Coord(x + 1, y + 1))
        }
    }

    private fun contractGrid(cGrid: List<List<Int>>, size: Int): List<List<Int>> {
        return cGrid
            .dropLast(1)
            .mapIndexed { index, row ->
                List(row.size) { i ->
                    row[i] + grid[index + size - 1][i]
                }
            }
    }


    override fun part1(): Coord {
        // Part 1
        var p1 = Maximum(Coord(0, 0), 0, 3)
        grid
            .windowed(3)
            .map { w ->
                w[0].zip(w[1]).map { it.first + it.second }.zip(w[2]).map { it.first + it.second }
            }.forEachIndexed { y, row ->
                row.windowed(3).forEachIndexed { x, group ->
                    val sum = group.sum()
                    if (sum > p1.power) p1 = Maximum(Coord(x + 1, y + 1), sum, 3)
                }
            }
       return p1.c
    }

    override fun part2(): Pair<Coord, Int> {
        // Part 2
        var p2 = Maximum(Coord(0, 0), 0, 0)
        var cGrid = grid
        for (size in 1..grid.size) {
            cGrid.forEachIndexed { y, row ->
                var power = row.take(size).sum()
                var x = size
                while (x < row.size) {
                    if (power > p2.power) p2 = Maximum(Coord(x + 1 - size, y + 1), power, size)
                    power = power + row[x] - row[x - size]
                    x++
                }
            }
            cGrid = contractGrid(cGrid, size + 1)
        }
        return p2.c to p2.size
    }
}

fun main() = Day.runDay(Y2018D11::class)

//    Class creation: 25ms
//    Part 1: (235, 48) (69ms)
//    Part 2: ((285, 113), 11) (422ms)
//    Total time: 517ms