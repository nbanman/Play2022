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

    // generate subsequent generations of plants
    private val generator = generateSequence(initialRow) { it ->
        val plants = "..$it.." // pad the row to account for growth
        plants // build the next generation
            .mapIndexed { index, _ ->
                val pattern = buildString {
                    append(if (index - 2 < 0) '.' else plants[index - 2])
                    append(if (index - 1 < 0) '.' else plants[index - 1])
                    append(plants[index])
                    append(if (index + 1 > plants.lastIndex) '.' else plants[index + 1])
                    append(if (index + 2 > plants.lastIndex) '.' else plants[index + 2])
                }
                if (commands[pattern] != null) {
                    '#'
                } else {
                    '.'
                }
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
        // same, we can surmise that the growth has stabilized. We then grab the first two values of that first 
        // stable group, along with their indices in the original sequence. That provides enough information to 
        // solve part 2.
        val firstRepeat = generator
            .withIndex()
            .map { (index, value) -> IndexedValue(index, value.sumOfPotNumbers(index)) }
            .windowed(10)
            .first { five ->
                five
                    .zipWithNext()
                    .map { (prev, next) -> next.value - prev.value }
                    .eachCount()
                    .size == 1
            }.take(2)

        // the last "chaotic" value obtained by the generator
        val lastUnstableValue = firstRepeat.first().value
        // the generation of this last chaotic value, so we don't double count generations when applying the stable
        // generation count
        val repeatIndex = firstRepeat.first().index
        // the amount that each successive generation adds to the pot number count
        val stableIncrement = firstRepeat.let { it.last().value - it.first().value }
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