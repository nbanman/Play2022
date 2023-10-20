package org.gristle.adventOfCode.y2021.d23

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.Graph
import org.gristle.adventOfCode.utilities.isEven
import org.gristle.adventOfCode.utilities.transpose
import org.gristle.adventOfCode.y2021.d23.Y2021D23b.State.Companion.isRoom
import kotlin.math.abs

class Y2021D23b(input: String) : Day {

    interface Place {
        val amphipod: Int

        class Room(val id: Int, val bucket: Int) : Place {

            val spots: List<Int> = List((bucket and 3) + 1) { i ->
                bucket.shr(2 + i * 2).and(3)
            }

            fun addBucket(amphipod: Int): Int {
                return bucket
                    .shr(3) // remove spot count
                    .shl(2) // add placeholder 0s at end for new amphipod
                    .and(amphipod) // replace placeholder with amphipod code
                    .shl(3) // add placeholder 0s at end for new spot count
                    .and(spots.size + 1) // replace placeholder with new spot count
            }

            fun isFinished(bucketSize: Int): Boolean {
                return spots.size == bucketSize && spots.all { it == id }
            }


            fun isOpen(bucketSize: Int): Boolean = spots.size < bucketSize && spots.all { it == id }

            fun steps(bucketSize: Int): Int = bucketSize - spots.size

            override val amphipod = spots[0]
        }

        class Hallway(override val amphipod: Int) : Place
    }

    @JvmInline
    value class State(val value: Long) : Iterable<Place> {

        val indices: IntRange
            get() = 0..10

        companion object {
            fun Int.isRoom() = isEven() && this >= 2 && this <= 8
        }

        fun add(i: Int, amphipod: Int): State {
            return if (i.isRoom()) {
                val room = getRoom(i)
                val offset = room.id * 11
                State(value - room.bucket * offset + room.addBucket(amphipod) * offset)
            } else {
                // doesn't work because representation is wrong. check to see if other places ask for a 0 value amphipod
                val offset = if (i in 3..9) i / 2 + 1 else i
                val intValue = amphipod shl (44 + offset)
                State(value + intValue)
            }
        }

        fun remove(i: Int): State = this

        fun getRoom(i: Int): Place.Room {
            val bucket = (value.shr((i / 2 - 1) * 11) and 1023L).toInt()
            return Place.Room(i / 2, bucket)
        }

        fun getHallway(i: Int): Place.Hallway {
            val offset = if (i <= 1) i * 2 else (i - 7) * 2
            val amphipod = (value.shr(44 + offset)) and 2
            return Place.Hallway(amphipod.toInt())
        }

        fun move(from: Int, to: Int, bucketSize: Int): Graph.Edge<State> {
            var steps = abs(from - to)
            val fromPlace = get(from)
            if (fromPlace is Place.Room) steps += 1 + fromPlace.steps(bucketSize)
            if (to.isRoom()) steps += 1 + getRoom(to).steps(bucketSize)
            val newState = remove(from).add(to, fromPlace.amphipod)
            return Graph.Edge(newState, steps.toDouble())
        }

        operator fun get(i: Int): Place = if (i.isRoom()) getRoom(i) else getHallway(i)

        override fun iterator() = object : Iterator<Place> {
            private var index = 0
            override fun hasNext(): Boolean = index <= 10

            override fun next(): Place = get(index++)
        }

        override fun toString(): String = buildString {
            val state = this@State
            println(this@State.value.toString(2))
            append("#############\n#")
            generateSequence(value.shr(44)) { it.shr(3) }
                .onEach {
                    val symbol = (it and 7)
                        .toInt()
                        .let {
                            if (it == 0) '.' else (it + 65).toChar()
                        }
                    append(symbol)
                }
                .take(11)
                .last()
            append("#\n")

            val t1 = List(4) { i -> getRoom(i).spots.map { ((it and 7) + 65).toChar() } }
            val t2 = t1.transpose()
            val t3 = t2.map { it.joinToString("#") }

            val amphipods: List<String> =
                List(4) { i -> getRoom(i).spots.map { ((it and 7) + 65).toChar() } }
                    .transpose()
                    .map { it.joinToString("#") }

            amphipods.forEach { append("###$it###\n") }

            append("#############")
        }
    }

    private val strings = input
        .filter { it in "ABCD\n" }
        .split('\n')
        .filter { it.isNotBlank() }

    private fun initialState(roomStrings: List<String>): State = roomStrings
        .transpose()
        .foldIndexed(0L) { roomIndex, state, bucket ->
            val roomState = bucket.foldIndexed(roomStrings.size) { index, acc, c ->
                acc + (c.code - 65).shl(index * 2 + 3)
            }
            state + roomState.shl(roomIndex * 11)
        }.let { State(it) }

    private fun totalEnergy(roomStrings: List<String>): Int {
        val bucketSize = roomStrings.size
        val initialState = initialState(roomStrings)

        fun attemptPlacement(
            freePlaces: Map<Int, IntRange>,
            stateIndex: Int,
            state: State,
            place: Place
        ): Graph.Edge<State>? {
            freePlaces[stateIndex]
                ?.filter { it.isRoom() }
                ?.forEach { roomIndex ->
                    val room = state.getRoom(roomIndex)
                    if (room.id == place.amphipod && room.isOpen(bucketSize))
                        return state.move(stateIndex, roomIndex, bucketSize)
                }
            return null
        }

        fun edges(state: State): List<Graph.Edge<State>> {
            val freePlaces: Map<Int, IntRange> = buildMap {
                var start = 0
                for (stateIndex in state.indices) {
                    if (!stateIndex.isRoom() && state.getHallway(stateIndex).amphipod != 0) {
                        val end = stateIndex - 1

                        // if the range has any size... 
                        if (end > start) {
                            val range = start..end // create range
                            range.forEach { put(it, range) } // populate map
                            start = stateIndex + 1 // reset start
                        }
                    }
                }
                if (start < 11) {
                    val range = start..10 // create range
                    range.forEach { put(it, range) } // populate map
                }
            }

            // roam halls looking for amphipods to place
            state.forEachIndexed { stateIndex, place ->
                if (place is Place.Room) { // rooms
                    if (!place.isFinished(bucketSize)) {
                        if (place.amphipod != place.id) {
                            attemptPlacement(freePlaces, stateIndex + 1, state, place)
                                ?.let { return listOf(it) }
                                ?: attemptPlacement(freePlaces, stateIndex - 1, state, place)
                                    ?.let { return listOf(it) }
                        }
                    }
                } else if (place is Place.Hallway) { // else halls
                    attemptPlacement(freePlaces, stateIndex, state, place)?.let { return listOf(it) }
                }
            }

            // Move to popping...
            return buildList {
                state.indices.filter { it.isRoom() }.forEach { roomIndex ->
                    freePlaces[roomIndex]
                        ?.filter { !it.isRoom() }
                        ?.forEach { hallIndex ->
                            add(state.move(roomIndex, hallIndex, bucketSize))
                        }
                }
            }
        }

        println(initialState)

        return 0
    }

    override fun part1(): Int = totalEnergy(strings)

    override fun part2(): Int {
        val expandedStrings = strings.dropLast(1) + listOf("DCBA", "DBAC") + strings.last()
        return totalEnergy(expandedStrings)
    }
}

fun main() = Day.runDay(Y2021D23b::class)