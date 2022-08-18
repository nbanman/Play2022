package org.gristle.adventOfCode.y2021.d23

import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.readRawInput

object Y2021D23 {
    private val data = readRawInput("y2021/d23")

    enum class S(val energy: Double) {
        A(1.0),
        B(10.0),
        C(100.0),
        D(1000.0),
        E(0.0) {
            override fun toString() = "_"
        }
    }

    fun <T> List<T>.swapElementAt(index: Int, newElement: T?): List<T> =
        newElement?.let { subList(0, index) + it + subList(index + 1, lastIndex + 1) } ?: this


    data class Room(val id: S, val spots: List<S>) {
        val hallIndex = id.ordinal + 1
        val isFinished = spots.all { it == id }
        val isOpen = spots.all { it == S.E || it == id } && spots.any { it == S.E }
        val isMixed = spots.any { it !in listOf(S.E, id) }
        val openings = spots.count { it == S.E }

        val topAmphipod = spots.find { it != S.E } ?: S.E

        fun addAmphipod(): Room {
            val lastEmptyIndex = spots.lastIndexOf(S.E)
            return copy(spots = spots.swapElementAt(lastEmptyIndex, id))
        }

        fun removeAmphipod(): Room {
            val firstOccupiedIndex = spots.indexOfFirst { it != S.E }
            return copy(spots = spots.swapElementAt(firstOccupiedIndex, S.E))
        }

        override fun toString(): String {
            return "$id: $spots"
        }
    }

    data class State(
        val hallway: List<S>,
        val roomA: Room,
        val roomB: Room,
        val roomC: Room,
        val roomD: Room,
        val log: String = ""
    ) {
        companion object {
            fun of(hallway: List<S>, roomList: List<Room>) =
                State(hallway, roomList[0], roomList[1], roomList[2], roomList[3])
        }

        private val roomList = listOf(roomA, roomB, roomC, roomD)

        val isEnd = hallway.all { it == S.E } && roomList.all { it.isFinished }

        fun edges(): List<Graph.Edge<State>> {

            // find open rooms and get corresponding values
            roomList.filter { it.isOpen }.forEach { room ->
                // look left
                var steps = 1
                for (hallSpot in room.hallIndex downTo 0) {
                    // look in hall first
                    when (hallway[hallSpot]) {
                        room.id -> return listOf(edgeFromHallToRoom(hallSpot, room, steps)) // found a piece to place
                        S.E -> {} // keep going
                        else -> break // left blocked; go right
                    }
                    // look in room (if available)
                    val roomSpot = hallSpot - 2
                    if (roomSpot >= 0) {
                        val top = roomList[roomSpot].topAmphipod
                        if (top == room.id) {
                            return listOf(edgeFromRoomToRoom(roomSpot, room, steps))
                        }
                    }
                    steps = if (hallSpot == 1) steps + 1 else steps + 2
                }
                // look right
                steps = 1
                for (hallSpot in (room.hallIndex + 1)..hallway.lastIndex) {
                    // look in hall first
                    when (hallway[hallSpot]) {
                        room.id -> return listOf(edgeFromHallToRoom(hallSpot, room, steps))
                        S.E -> {}
                        else -> break // right blocked; go to pop
                    }
                    // look in room (if available)
                    val roomSpot = hallSpot - 1
                    if (roomSpot <= roomList.lastIndex) {
                        val top = roomList[roomSpot].topAmphipod
                        if (top == room.id) {
                            return listOf(edgeFromRoomToRoom(roomSpot, room, steps))
                        }
                    }
                    steps = if (hallSpot == 5) steps + 1 else steps + 2
                }
            }

            // Move to popping...

            // Create mutableList for holding various edges
            val edges = mutableListOf<Graph.Edge<State>>()

            roomList.filter { it.isMixed }.forEach { room ->
                // look left
                var steps = 1
                for (hallSpot in room.hallIndex downTo 0) {
                    if (hallway[hallSpot] == S.E) edges.add(edgeFromRoomToHall(hallSpot, room, steps)) else break
                    steps = if (hallSpot == 1) steps + 1 else steps + 2
                }
                // look right
                steps = 1
                for (hallSpot in (room.hallIndex + 1)..hallway.lastIndex) {
                    if (hallway[hallSpot] == S.E) edges.add(edgeFromRoomToHall(hallSpot, room, steps)) else break
                    steps = if (hallSpot == 5) steps + 1 else steps + 2
                }
            }
            return edges
        }

        private fun edgeFromRoomToHall(hallSpot: Int, room: Room, steps: Int): Graph.Edge<State> {
            val newAmphipod = room.topAmphipod
            val newHallway = hallway.swapElementAt(hallSpot, newAmphipod)
            val newLog = "$newAmphipod from Room ${room.id} to Hall $hallSpot"
            val newState = when (room.id) {
                S.A -> copy(hallway = newHallway, roomA = room.removeAmphipod(), log = newLog)
                S.B -> copy(hallway = newHallway, roomB = room.removeAmphipod(), log = newLog)
                S.C -> copy(hallway = newHallway, roomC = room.removeAmphipod(), log = newLog)
                S.D -> copy(hallway = newHallway, roomD = room.removeAmphipod(), log = newLog)
                S.E -> this
            }
            val weight = (steps + room.openings + 1) * newAmphipod.energy
            return Graph.Edge(newState, weight)
        }

        private fun edgeFromHallToRoom(hallSpot: Int, room: Room, steps: Int): Graph.Edge<State> {
            val newHallway = hallway.swapElementAt(hallSpot, S.E)
            val newLog = "${room.id} from Hall $hallSpot to Room ${room.id}"
            val newState = when (room.id) {
                S.A -> copy(hallway = newHallway, roomA = room.addAmphipod(), log = newLog)
                S.B -> copy(hallway = newHallway, roomB = room.addAmphipod(), log = newLog)
                S.C -> copy(hallway = newHallway, roomC = room.addAmphipod(), log = newLog)
                S.D -> copy(hallway = newHallway, roomD = room.addAmphipod(), log = newLog)
                S.E -> this
            }
            val weight = (steps + room.openings) * room.id.energy
            return Graph.Edge(newState, weight)
        }

        private fun edgeFromRoomToRoom(roomSpot: Int, room: Room, steps: Int): Graph.Edge<State> {
            val other = roomList[roomSpot]
            val newLog = "${other.topAmphipod} from Room ${other.id} to Room ${room.id}"
            val newState = when (room.id) {
                S.A -> when (roomList[roomSpot].id) {
                    S.B -> copy(roomA = room.addAmphipod(), roomB = other.removeAmphipod(), log = newLog)
                    S.C -> copy(roomA = room.addAmphipod(), roomC = other.removeAmphipod(), log = newLog)
                    S.D -> copy(roomA = room.addAmphipod(), roomD = other.removeAmphipod(), log = newLog)
                    else -> throw IllegalArgumentException()
                }
                S.B -> when (roomList[roomSpot].id) {
                    S.A -> copy(roomB = room.addAmphipod(), roomA = other.removeAmphipod(), log = newLog)
                    S.C -> copy(roomB = room.addAmphipod(), roomC = other.removeAmphipod(), log = newLog)
                    S.D -> copy(roomB = room.addAmphipod(), roomD = other.removeAmphipod(), log = newLog)
                    else -> throw IllegalArgumentException()
                }
                S.C -> when (roomList[roomSpot].id) {
                    S.A -> copy(roomC = room.addAmphipod(), roomA = other.removeAmphipod(), log = newLog)
                    S.B -> copy(roomC = room.addAmphipod(), roomB = other.removeAmphipod(), log = newLog)
                    S.D -> copy(roomC = room.addAmphipod(), roomD = other.removeAmphipod(), log = newLog)
                    else -> throw IllegalArgumentException()
                }
                S.D -> when (roomList[roomSpot].id) {
                    S.A -> copy(roomD = room.addAmphipod(), roomA = other.removeAmphipod(), log = newLog)
                    S.B -> copy(roomD = room.addAmphipod(), roomB = other.removeAmphipod(), log = newLog)
                    S.C -> copy(roomD = room.addAmphipod(), roomC = other.removeAmphipod(), log = newLog)
                    else -> throw IllegalArgumentException()
                }
                S.E -> throw IllegalArgumentException()
            }
            val weight = (steps + other.openings + 1 + room.openings + 1) * room.id.energy
            return Graph.Edge(newState, weight)
        }

        override fun toString(): String {
            return "H: $hallway $roomA $roomB $roomC $roomD"
        }
    }

    private fun totalEnergy(state: State): Long {
        val distance = Graph.dijkstra(state, { it.isEnd }) {
            it.edges()
        }
        return distance.last().weight.toLong()
    }

    private fun getState(a: List<String>): State {
        return a.let {
            List(it.size * it.first().length) { i ->
                S.valueOf(it[i % it.size][i / it.size].toString())
            }.chunked(it.size)
        }.mapIndexed { index, s ->
            val name = S.values()[index]
            Room(name, s)
        }.let { State.of(List(7) { S.E }, it) }
    }

    private fun strings() = data
        .filter { it in "ABCD\n" }
        .split('\n')
        .filter { it.isNotBlank() }

    fun part1(): Long {
        val state = getState(strings())
        return totalEnergy(state)
    }

    fun part2(): Long {
        val strings = strings()
            .let {
                val last = it.last()
                it.dropLast(1) + listOf("DCBA", "DBAC") + last
            }
        val state = getState(strings)
        return totalEnergy(state)
    }
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2021D23.part1()} (${elapsedTime(time)}ms)") // 14148
    time = System.nanoTime()
    println("Part 2: ${Y2021D23.part2()} (${elapsedTime(time)}ms)") // 43814
}