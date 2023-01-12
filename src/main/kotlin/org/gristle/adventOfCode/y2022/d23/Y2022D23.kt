package org.gristle.adventOfCode.y2022.d23

import org.gristle.adventOfCode.utilities.*

class Y2022D23(input: String) {

    enum class Dir(val nsew: Nsew, val eval: List<Coord>) {
        N(Nsew.NORTH, listOf(Coord(-1, -1), Coord(0, -1), Coord(1, -1))),
        S(Nsew.SOUTH, listOf(Coord(-1, 1), Coord(0, 1), Coord(1, 1))),
        W(Nsew.WEST, listOf(Coord(-1, -1), Coord(-1, 0), Coord(-1, 1))),
        E(Nsew.EAST, listOf(Coord(1, -1), Coord(1, 0), Coord(1, 1)));

        fun advance(n: Int): Dir = values()[(ordinal + n) % 4]
    }

    private val grove = input
        .lines()
        .flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, ch -> if (ch == '#') Coord(x, y) else null }
        }.toSet()

    private fun Set<Coord>.move(dir: Dir): Set<Coord> {
        val proposedMoves = mapNotNull { elf ->
            (0..3).firstNotNullOfOrNull { i ->
                val currDir = dir.advance(i)
                if (elf.getNeighbors(true).intersect(this).isEmpty()) {
                    elf to elf
                } else if (intersect(currDir.eval.map { it + elf }).isEmpty()) {
                    elf.move(currDir.nsew) to elf
                } else if (i == 3) {
                    elf to elf
                } else {
                    null
                }
            }
        }

        val uniques = buildSet { for ((move, _) in proposedMoves) if (!add(move)) remove(move) }

        val moved = proposedMoves
            .map { (proposed, original) ->
                if (proposed in uniques) proposed else original
            }.toSet()
        return moved
    }

    private fun Iterable<Coord>.asList(): List<Boolean> {
        val (xRange, yRange) = minMaxRanges()
        val list: List<Boolean> = buildList {
            for (y in yRange) for (x in xRange) {
                add(this@asList.contains(Coord(x, y)))
            }
        }
        return list
    }

    private val movement = generateSequence(grove to Dir.N) { (prev, dir) ->
        val next = prev.move(dir)
        next to dir.advance(1)
    }

    fun part1(): Int {
        val elvesAtRest = movement
            .take(11)
            .last()
            .first

        val listOfElves = elvesAtRest.asList()

        return listOfElves.size - elvesAtRest.size
    }

    fun part2(): Int {
        return movement
            .withIndex()
            .zipWithNext()
            .first { (prev, next) ->
                prev.value.first == next.value.first
            }.first
            .index + 1
    }
}

fun main() {
    val input = getInput(23, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D23(input)
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 3812 (10228ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 1003 (374635ms)!!!
    println("Total time: ${timer.elapsed()}ms")
}