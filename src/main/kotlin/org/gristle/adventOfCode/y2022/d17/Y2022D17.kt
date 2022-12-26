package org.gristle.adventOfCode.y2022.d17

import org.gristle.adventOfCode.utilities.*
import kotlin.math.max

class Y2022D17(private val jetPattern: String) {

    val blockString = """####

.#.
###
.#.

..#
..#
###

#
#
#
#

##
##"""

    data class Block(val grid: Grid<Boolean>, val bl: Coord = Coord.ORIGIN) {

        fun shift(chamber: Set<Coord>, check: Int, direction: Int): Block {
            val checkResult = if (direction > 0) {
                bl.x + grid.width == check
            } else {
                bl.x == check
            }
            return if (checkResult) this else {
                val proposed = grid.coords().filter { grid[it] }.map { it.copy(x = it.x + direction) + bl }
                if (proposed.intersect(chamber).isNotEmpty()) {
                    this
                } else {
                    copy(bl = bl.copy(x = bl.x + direction))
                }
            }
        }

        fun left(chamber: Set<Coord>) = shift(chamber, 0, -1)
        fun right(chamber: Set<Coord>) = shift(chamber, 7, 1)

        fun drop(chamber: Set<Coord>): Block? {
            return if (bl.y == 0) null else {
                val proposed = grid.coords().filter { grid[it] }.map { it.copy(y = it.y - 1) + bl }
                if (proposed.intersect(chamber).isNotEmpty()) {
                    null
                } else {
                    copy(bl = bl.copy(y = bl.y - 1))
                }
            }
        }

        fun place(bl: Coord) = copy(bl = bl)

        fun coords() = grid.coords().filter { grid[it] }.map { it + bl }
    }

    private val blocks = blockString.split("\n\n").map { s -> Block(s.toGrid { it == '#' }.flipX()) }

    //    private val blocks = listOf(
//        listOf(Coord(0, 0), Coord(1, 0), Coord(2, 0), Coord(3, 0)),
//        listOf(Coord(1, 0), Coord(0, 1), Coord(1, 1), Coord(2, 1), Coord(1, 2)),
//        listOf(Coord(0, 0), Coord(1, 0), Coord(2, 0), Coord(2, 1), Coord(2, 2)),
//        listOf(Coord(0, 0), Coord(0, 1), Coord(0, 2), Coord(0, 3)),
//        listOf(Coord(0, 0), Coord(1, 0), Coord(0, 1), Coord(1, 1)),
//    )
    fun part1(): Int {
        val chamber = mutableSetOf<Coord>()
        var highest = 0
        var rockCount = 0
        var windCount = 0
        fun blockSequence(): Block {
            val nextBlock = blocks[rockCount % 5].place(Coord(2, highest + 3))
            return generateSequence(nextBlock) {
                val shiftedBlock = if (jetPattern[windCount % jetPattern.length] == '<') {
                    it.left(chamber)
                } else {
                    it.right(chamber)
                }
                windCount++
                val drop = shiftedBlock.drop(chamber)
                if (drop == null) {
                    chamber.addAll(shiftedBlock.coords())
                    highest = max(highest, shiftedBlock.bl.y + shiftedBlock.grid.height)
                    null
                } else {
                    drop
                }
            }.last()
        }

        var first: Block? = null
        val repeat = generateSequence { blockSequence() }
            .onEachIndexed { index, block ->
//                println("$index, height: $highest")
//                chamber.toGraphicString().lines().reversed().testPrint()
                if (index == 0) {
                    val shiftedBlock = if (jetPattern[(windCount - 1) % jetPattern.length] == '<') {
                        block.copy(bl = block.bl.copy(x = block.bl.x - 1))
                    } else {
                        block.copy(bl = block.bl.copy(x = block.bl.x + 1))
                    }

                    first = shiftedBlock
                }
                rockCount++
            }.withIndex()
            .first { (index, block) ->
                index != 0 && (first?.bl?.x == block.bl.x ?: false) && (first?.grid == block.grid ?: false)
            }
        chamber.toGraphicString().lines().reversed().testPrint()
        return highest
    }

    fun part2() = "To be implemented"
}

fun main() {
    val input = listOf(
        getInput(17, 2022),
        """>>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>""",
    )
    val timer = Stopwatch(start = true)
    val solver = Y2022D17(input[0])
    println("Class creation: ${timer.lap()}ms")
    println("\tPart 1: ${solver.part1()} (${timer.lap()}ms)") // 3055
    println("\tPart 2: ${solver.part2()} (${timer.lap()}ms)") // 
    println("Total time: ${timer.elapsed()}ms")
}