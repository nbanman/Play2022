package org.gristle.adventOfCode.y2018.d12

import org.gristle.adventOfCode.utilities.eachCount
import org.gristle.adventOfCode.utilities.elapsedTime
import org.gristle.adventOfCode.utilities.lines
import org.gristle.adventOfCode.utilities.readRawInput

class Y2018D12(input: String) {
    private val lines = input.lines()

    // parse initial row of plants
    private val initialRow = lines[0].drop(15)

    // parse commands into translation map
    private val commands: Map<String, Char> = buildMap {
        lines
            .drop(2)
            .filter { it.last() == '#' }
            .forEach { put(it.take(5), it.last()) }
    }

    // sequence that provides successive generations of plant rows
    private val generator = generateSequence(initialRow) { it ->
        val plants = "..$it.." // pad the row to account for growth
        plants // build the next generation
            .mapIndexed { index, _ ->
                val pattern = buildString { // make a pattern using the spot and the spots around it
                    append(if (index - 2 < 0) '.' else plants[index - 2])
                    append(if (index - 1 < 0) '.' else plants[index - 1])
                    append(plants[index])
                    append(if (index + 1 > plants.lastIndex) '.' else plants[index + 1])
                    append(if (index + 2 > plants.lastIndex) '.' else plants[index + 2])
                }
                // use the translation map with the pattern to determine if there is a plant at that spot
                if (commands[pattern] != null) '#' else '.'
            }.joinToString("")
    }

    /**
     * take the row and obtain the sum of pot numbers. "generations" is used to account for growth of the row
     */
    private fun String.sumOfPotNumbers(generations: Int) =
        mapIndexed { index, c -> if (c == '#') (index - (generations * 2)) else 0 }
            .sum()

    fun part1(): Int {
        val generations = 20
        // take the sequence, run it 21 times (first time yields the initial row), then get the pot number sum
        // of the last row generation generated.
        return generator
            .take(generations + 1) // the generator also includes the original row, thus generations + 1
            .last() // terminate sequence on last row
            .sumOfPotNumbers(generations) // get sum of pot numbers
    }

    fun part2(): Long {
        // too many generations to naively compute!
        val generations = 50_000_000_000

        // upon observation, the growth is chaotic at first but then finds a stable pattern where growth is constant.
        // Thus, the strategy is to look at generations 10 at a time. When the difference between each is the 
        // same, we can surmise that the growth has stabilized. That group provides enough information to 
        // solve part 2.

        val groupSize = 10
        val firstStable = generator
            .withIndex()
            .map { (index, value) -> IndexedValue(index, value.sumOfPotNumbers(index)) }
            .windowed(groupSize)
            .first { group ->
                group
                    .zipWithNext()
                    .map { (prev, next) -> next.value - prev.value }
                    .eachCount()
                    .size == 1
            }

        // the last "chaotic" value obtained by the generator
        val lastUnstableValue = firstStable.first().value
        // the generation of this last chaotic value, so we don't double count generations when applying the stable
        // generation count
        val repeatIndex = firstStable.first().index
        // the amount that each successive generation adds to the pot number count
        val stableIncrement = firstStable.let { it[1].value - it[0].value }
        // putting it all together
        return lastUnstableValue + stableIncrement * (generations - repeatIndex)
    }
}

fun main() {
    var time = System.nanoTime()
    val c = Y2018D12(readRawInput("y2018/d12"))
    println("Class creation: ${elapsedTime(time)}ms")
    time = System.nanoTime()
    println("Part 1: ${c.part1()} (${elapsedTime(time)}ms)") // 4110
    time = System.nanoTime()
    println("Part 2: ${c.part2()} (${elapsedTime(time)}ms)") // 2650000000466
}