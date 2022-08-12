package org.gristle.adventOfCode.y2016.d11

import org.gristle.adventOfCode.utilities.*

object Y2016D11 {
    private val input = readRawInput("y2016/d11")

    private val pattern = """(\w+)(?: |-compatible )(generator|microchip)"""

    /*
    * The state, showing what's on each floor and where the elevator is.
    */
    private data class FloorState(val elevator: Int, val floors: List<Floor>) {

        fun isValid() = floors.all { it.isValid() } // Checks that all floors are valid
        fun isSolved() = toHash().drop(1).dropLast(2).all { it == '0' } // Checks if all items on top floor

        // Gives "hash" of the state, which is just numbers corresponding to the elevator position + number of items
        // on each floor. Since items are interchangeable and all microchips must be matched where there are
        // generators, all similarly hashed states are functionally identical.
        fun toHash() = "$elevator${ floors.map { it.toHash() }.joinToString("") }"

        // Generates the list of possible floor states after a valid move.
        fun validStates(): List<FloorState> {
            val vm = mutableListOf<FloorState>() // Holder for states to return

            // List of floor numbers the elevator can move to. Can go up or down, but can't go below 0 or above the top.
            val potentialFloorNumbers = listOf(elevator - 1, elevator + 1).filter { it in 0..floors.lastIndex }

            // Gets all potential loads the elevator can carry by getting combinations of all the items on the floor
            // then checking to see if the remaining items on the floor cause any chips to fry.
            val potentialLoads = getPotentialMoves(floors[elevator])
                .filter { Floor(floors[elevator].items - it.items).isValid() } // check for frying

            // Nested loop that tries all valid loads on all potential floors. If the floor with the new load is valid,
            // add it to the list of potential states.
            for (pfn in potentialFloorNumbers) {
                for (pm in potentialLoads) {
                    // This generates the floor layout of the state by taking the original state, then making
                    // modifications to the floor that the load is being moved to as well as the floor that the load
                    // is taken from.
                    val newFloors = floors.mapIndexed() { index, floor ->
                        when (index){
                            elevator -> Floor(floors[index].items - pm.items) // remove items from floor
                            pfn -> Floor(floors[index].items + pm.items) // add items to floor
                            else -> floor // keep floor unchanged
                        }
                    }
                    // take the floor layout and new elevator position and create a new state
                    val newState = FloorState(pfn, newFloors)
                    // if the new state is valid on all floors, add it.
                    if (newState.isValid()) vm.add(newState)
                }
            }
            return vm
        }

        // Gets combinations of potential items to move, with at least one item and at most two items.
        fun getPotentialMoves(floor: Floor): List<Floor> {
            return floor.items.foldIndexed(listOf<Floor>()) { index, acc, radioItem ->
                acc + Floor(listOf(radioItem)) + floor.items.drop(index + 1).map { Floor(listOf(radioItem, it)) }
            }
        }
    }

    private enum class ItemType { MICROCHIP, GENERATOR }

    private data class RadioItem(val name: String, val type: ItemType)

    private data class Floor(val items: List<RadioItem>) {
        val microchips = items.filter { it.type == ItemType.MICROCHIP }.map { it.name }
        val generators = items.filter { it.type == ItemType.GENERATOR }.map { it.name }
        fun isValid() = microchips.isEmpty() || generators.isEmpty() || ( generators.containsAll(microchips) )
        fun toHash() = "${ microchips.size }${ generators.size }"
    }

    // Runs a regex and creates the inital floor state from it. Each line in the input is a 'floor.' Each
    private fun parseFloors(input: String, part2: Boolean): FloorState {
        val floors = input
            .split('\n')
            .mapIndexed { index, line ->
                line
                    .groupValues(pattern)
                    .map { gv ->
                        val type = if (gv[1] == "microchip") ItemType.MICROCHIP else ItemType.GENERATOR
                        RadioItem(gv[0], type)
                    }
                    .let {
                        if (index == 0 && part2) it + listOf(
                            RadioItem("elerium", ItemType.GENERATOR),
                            RadioItem("elerium", ItemType.MICROCHIP),
                            RadioItem("dilithium", ItemType.GENERATOR),
                            RadioItem("dilithium", ItemType.MICROCHIP),
                        ) else it
                    }.let { Floor(it) }
            }
        return FloorState(0, floors)
    }

    /*
    * Solver
    */
    private fun solveFloors(s: FloorState): Int {
        val stateHashes = mutableListOf<String>(s.toHash())

        /*
        * Recursive breadth-first search through all possible states, pruning invalid states and states that are
        * functionally identical to previous states.
        */
        tailrec fun solve(states: List<FloorState>, counter: Int = 0): Int {
            // Holder for all new states found.
            val newStates = mutableListOf<FloorState>()
            // Iterate through all states that have been found so far. First pass will be just the initial state, but
            // later passes will include all valid states derivable from that initial state.
            for (state in states) {
                // Get the list of valid states and then prune.
                val validStates = state.validStates().filter { !stateHashes.contains(it.toHash()) }
                // If no new valid states found, move on to next state.
                if (validStates.isEmpty()) continue
                // If solved, return the number of steps.
                if (validStates.any { it.isSolved() }) {
                    return counter + 1
                }
                // Add all new valid states and add their hashes to the hash registry.
                newStates.addAll(validStates)
                stateHashes.addAll(validStates.map { it.toHash() })
            }
            // If all end up in a dead-end, unsolvable.
            return if (newStates.isEmpty()) {
                0
            } else {
                // Run the search on the new states found.
                solve(newStates, counter + 1)
            }
        }
        return solve(listOf(s))
    }

    fun part1() = solveFloors(parseFloors(input, false))

    fun part2() = solveFloors(parseFloors(input, true))
}

fun main() {
    var time = System.nanoTime()
    println("Part 1: ${Y2016D11.part1()} (${elapsedTime(time)}ms)") // 47
    time = System.nanoTime()
    println("Part 2: ${Y2016D11.part2()} (${elapsedTime(time)}ms)") // 71
}