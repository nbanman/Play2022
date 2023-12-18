package org.gristle.adventOfCode.y2023.d18

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Nsew
import kotlin.math.abs

class Y2023D18(input: String) : Day {
    
    class Plan(val dir: Nsew, val dist: Int, color: String) {
        val colorDir = when(color.last()) {
            '0' -> Nsew.EAST
            '1' -> Nsew.SOUTH
            '2' -> Nsew.WEST
            else -> Nsew.NORTH
        }
        val colorDist = color.dropLast(1).toInt(16)
    }

    private val plans = input.lines().map {
        val (dir, dist, color) = it.split(" (#", " ", ")")
        Plan(Nsew.of(dir[0]), dist.toInt(), color)
    }

    override fun part1(): Int {
        val moat = plans.runningFold(Coord.ORIGIN) { acc, plan -> acc.move(plan.dir, plan.dist) }
        val moatSize = plans.sumOf { it.dist }
        val moatArea = abs((moat + moat[0]).zipWithNext { (x1, y1), (x2, y2) -> x1 * y2 - x2 * y1 }.sum() / 2)
        return moatSize + 1 + (moatArea - (moatSize) / 2)
    }

    override fun part2(): Long {
        val moat = plans.runningFold(Coord.ORIGIN) { acc, plan ->
            acc.move(plan.colorDir, plan.colorDist) 
        }
        val moatSize: Long = plans.sumOf { it.dist.toLong() }
        val moatArea = abs((moat + moat[0])
            .zipWithNext { (x1, y1), (x2, y2) -> x1.toLong() * y2 - x2.toLong() * y1 }
            .fold(0L, Long::plus) / 2)
        return moatSize + 1 + (moatArea - (moatSize) / 2)
    }
}

fun main() = Day.runDay(Y2023D18::class)

//    Class creation: 17ms
//    Part 1: 50746 (3ms)
//    Part 2: 70086216556038 (3ms)
//    Total time: 24ms

@Suppress("unused")
private val sampleInput = listOf(
    """R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
""",
)