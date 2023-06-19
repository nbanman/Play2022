package org.gristle.adventOfCode.y2016.d17

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Coord
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.contains
import org.gristle.adventOfCode.utilities.md5

class Y2016D17(private val input: String) : Day {

    // state object for BFS search
    private data class Room(val pos: Coord, val passCode: String)

    // used to translate whether a door is open given a specific MD5 hash
    private val openRange = 'b'..'f'

    // the bottom-right room position
    private val endPos = Coord(3, 3)

    // function finds edges for a given Room node
    private val openDoors = { room: Room ->

        if (room.pos == endPos) {

            // If the node is at endPos, we do not want to return any neighbors. That path is completed. Relevant for
            // part 2 because we are not done when we first hit endPos.
            emptyList()
        } else {

            // use MD5 on the passcode to determine which doors are open and return list of rooms who have open
            // doors leading to them.
            val open = room.passCode.md5().take(4).map { it in openRange }
            listOf(
                Room(room.pos.north(), room.passCode + 'U'),
                Room(room.pos.south(), room.passCode + 'D'),
                Room(room.pos.west(), room.passCode + 'L'),
                Room(room.pos.east(), room.passCode + 'R'),
            ).filter {
                it.pos in (Coord.ORIGIN to endPos) &&
                        when (it.passCode.last()) {
                            'L' -> open[2]
                            'R' -> open[3]
                            'U' -> open[0]
                            else -> open[1]
                        }
            }
        }
    }

    // BFS used to explore the vault
    private val explore = Graph
        .bfsSequence(
            startId = Room(Coord.ORIGIN, input),
            defaultEdges = openDoors
        )

    // Part one wants the fastest path, so BFS with an endCondition of reaching endPos finds it. The answer is the
    // passcode of the first node at the endPos, minus the prelude.
    override fun part1() = explore
        .first { it.id.pos == endPos }
        .id
        .passCode
        .drop(input.length)

    // Part two wants the longest path, so BFS until no more edges are found. The answer is the weight of the last
    // Node found at endPos.
    override fun part2() = explore
        .last { it.id.pos == endPos }
        .steps()
}

fun main() = Day.runDay(Y2016D17::class)

//    Class creation: 14ms
//    Part 1: DDRUDLRRRD (14ms)
//    Part 2: 398 (180ms)
//    Total time: 209ms