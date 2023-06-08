package org.gristle.adventOfCode.y2015.d17

import org.gristle.adventOfCode.Day
import org.gristle.adventOfCode.utilities.getIntList

class Y2015D17(input: String) : Day {

    // List of combinations of containers where the combination fits exactly 150L. Lazy b/c used by both parts
    private val containers: List<List<Int>> by lazy {

        // parses the container sizes from input
        val containers = input.getIntList().toMutableList()
        val storage = 150

        // combos stores list of all possible container combinations  
        val combos = mutableListOf<List<Int>>()

        // for each container, for each existing combination, add a new combination that uses the existing 
        // combination plus the container. The existing combinations remain as well. Then add the container as the
        // start of a new combination as well.
        for (container in containers) {

            // I use a for loop with indices because combos grows within the loop, but I only want to loop through 
            // the combos that were already created before the new container was considered.
            for (index in combos.indices) {
                if (combos[index].sum() + container <= storage) {
                    combos.add(combos[index] + container)
                }
            }

            // add the new container as a start to a new combination
            combos.add(listOf(container))
        }

        // we only want combinations that add up to 150L, so apply a filter and return
        combos.filter { it.sum() == storage }
    }

    override fun part1() = containers.size

    override fun part2(): Int {
        val minimumContainers = containers.minOf { it.size }
        return containers.filter { it.size == minimumContainers }.size
    }
}

fun main() = Day.runDay(Y2015D17::class)

//    Class creation: 13ms
//    Part 1: 1638 (26ms)
//    Part 2: 17 (0ms)
//    Total time: 40ms
