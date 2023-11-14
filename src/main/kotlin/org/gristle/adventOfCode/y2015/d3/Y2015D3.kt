package org.gristle.adventOfCode.y2015.d3

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.isEven
import org.gristle.adventOfCode.utilities.partitionIndexed

class Y2015D3(private val input: String) : Day {

    private fun deliver(s: String) = s
        .runningFold(Coord.ORIGIN) { santa, dir -> santa.move(dir) }
        .toSet()

    override fun part1() = deliver(input).size

    override fun part2(): Int = input
        .partitionIndexed { index, _ -> index.isEven() }
        .toList()
        .map(::deliver)
        .reduce(Set<Coord>::union)
        .size
}

fun main() = Day.runDay(Y2015D3::class)

//    Class creation: 21ms
//    Part 1: 2081 (8ms)
//    Part 2: 2341 (6ms)
//    Total time: 36ms