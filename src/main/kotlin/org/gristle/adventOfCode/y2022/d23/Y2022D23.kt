package org.gristle.adventOfCode.y2022.d23

import org.gristle.adventOfCode.utilities.*

typealias Grove = MutableGrid<Boolean>

class Y2022D23(input: String) {

    enum class Dir(val nsew: Nsew) {
        N(Nsew.NORTH),
        S(Nsew.SOUTH),
        W(Nsew.WEST),
        E(Nsew.EAST);

        fun advance(n: Int): Dir = values()[(ordinal + n) % 4]
    }

    val grove: Grove = input.toMutableGrid { it == '#' }

    fun Grove.xLimit(col: Int, direction: Int): Int {
        var x = col
        do {
            for (y in 0 until height) {
                if (this[y * width + x]) return x
            }
            x += direction
        } while (if (direction > 0) x < width else x >= 0)
        return -1
    }

    fun Grove.yLimit(row: Int, direction: Int): Int {
        var y = row
        do {
            for (x in 0 until width) {
                if (this[y * width + x]) return y
            }
            y += direction
        } while (if (direction > 0) y < height else y >= 0)
        return -1
    }

    fun Coord.adjacentEmpty(grove: Grove, dir: Dir): Boolean = when (dir) {
        Dir.N -> listOf(Coord(x - 1, y - 1), copy(y = y - 1), Coord(x + 1, y - 1))
        Dir.S -> listOf(Coord(x - 1, y + 1), copy(y = y + 1), Coord(x + 1, y + 1))
        Dir.W -> listOf(Coord(x - 1, y - 1), copy(x = x - 1), Coord(x - 1, y + 1))
        Dir.E -> listOf(Coord(x + 1, y - 1), copy(x = x + 1), Coord(x + 1, y + 1))
    }.all { !grove.validCoord(it) || !grove[it] }

    fun Coord.adjacentEmpty(grove: Grove): Boolean = listOf<Coord>(
        Coord(x - 1, y - 1), copy(y = y - 1), Coord(x + 1, y - 1),
        copy(x = x - 1), copy(x = x + 1),
        Coord(x - 1, y + 1), copy(y = y + 1), Coord(x + 1, y + 1),
    ).all { !grove.validCoord(it) || !grove[it] }

    fun Coord.moveElf(dir: Dir): Coord = when (dir) {
        Dir.N -> copy(y = y - 1)
        Dir.S -> copy(y = y + 1)
        Dir.W -> copy(x = x - 1)
        Dir.E -> copy(x = x + 1)
    }

    fun Grove.move(dir: Dir): Grove {
        val xMin = xLimit(0, 1)
        val xMax = xLimit(width - 1, -1)
        val yMin = yLimit(0, 1)
        val yMax = yLimit(height - 1, -1)

        val nextGrove: Grove = MutableGrid(xMax - xMin + 3, yMax - yMin + 3) { false }

        val offset = Coord(1 - xMin, 1 - yMin)

        coords().filter { this[it] }.forEach { pos ->
            val offsetPos = pos + offset

            if (pos.adjacentEmpty(this)) { // if no other elves adjacent...
                nextGrove[offsetPos] = true // stay in place
            } else {
                (0..3).firstOrNull { i -> // try to move in each direction...
                    val currDir = dir.advance(i)
                    if (pos.adjacentEmpty(this, currDir)) { // if no elves in that dir...
                        // check to see if another elf has already moved there
                        val prospect = offsetPos.moveElf(currDir)
                        if (nextGrove[prospect]) { // if elf already exists
                            nextGrove[offsetPos] = true // stay in place
                            nextGrove[prospect] = false // remove previous move
                            nextGrove[prospect.moveElf(currDir)] = true // put previous elf back in place
                        } else { // ...else move the elf there!
                            nextGrove[prospect] = true
                        }
                        true
                    } else if (i == 3) { // if we've reached the final direction to check... 
                        nextGrove[offsetPos] = true // stay in place
                        true
                    } else {
                        false
                    }
                } ?: let { // no move possible so stay in place 
                    nextGrove[offsetPos] = true
                }
            }
        }

        return nextGrove
    }

    val movement = generateSequence(grove to Dir.N) { (current, dir) -> current.move(dir) to dir.advance(1) }

    fun part1(): Int = movement
        .take(11)
        .last()
        .let { (grove, _) ->
            val width = grove.xLimit(grove.width - 1, -1) - grove.xLimit(0, 1) + 1
            val height = grove.yLimit(grove.height - 1, -1) - grove.yLimit(0, 1) + 1
            width * height - grove.count { it }
        }

    fun part2(): Int = movement
        .withIndex()
        .zipWithNext()
        .first { (prev, next) ->
            prev.value.first == next.value.first
        }.first
        .index + 1
}

fun main() {
    val input = getInput(23, 2022)
    val timer = Stopwatch(start = true)
    val solver = Y2022D23(input)
    println("Class creation: ${timer.lap()}ms") // 17ms
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 3812 (65ms) (original 10228ms)
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 1003 (697ms) (original 374635ms!!!)
    println("Total time: ${timer.elapsed()}ms") // 780ms
}

private val sinput = listOf(
    """....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#..""",
    """.....
..##.
..#..
.....
..##.
....."""
)
