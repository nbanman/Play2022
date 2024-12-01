package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.takeUntil

fun main() {
    val (input1, input2, input3) = ecInputs(24, 8)
    val timer = Stopwatch(true)
    println("1. ${part1(input1)}: ${timer.lap()}ms")
    println("2. ${part2(input2)}: ${timer.lap()}ms")
    println("3. ${part3(input3)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private data class Shrine(
    val blocks: Int = 1,
    val width: Int = 1,
    val innerDimensions: Int = 0,
    val height: Int = 1,
    val columns: List<Int> = listOf(1),
) {
    fun buildLevel(priests: Int, acolytes: Int, part3: Boolean = false): Shrine {
        val width = width + 2
        var height = (height * priests) % acolytes
        val columns: List<Int>
        val innerDimensions: Int
        if (part3) {
            height += acolytes
            columns = this.columns.toMutableList()
            columns.add(0)
            for (idx in columns.indices) {
                columns[idx] += height
            }
            val removal = { column: Int ->
                (((priests.toLong() * width) * column) % acolytes).toInt()
            }
            innerDimensions = removal(columns[0]) + columns
                .subList(1, columns.lastIndex)
                .map { column -> removal(column) * 2 }
                .sum()
        } else {
            columns = this.columns
            innerDimensions = this.innerDimensions
        }
        val blocks = height * width + this.blocks
        return Shrine(blocks, width, innerDimensions, height, columns)
    }
}

private fun part1(input: String): Int {
    val blocksAvailable = input.toInt()
    val priests = 1
    val acolytes = 2
    val shrine = build(priests, acolytes, blocksAvailable)
    return shrine.width * (shrine.blocks - blocksAvailable)
}

private fun part2(input: String): Int {
    val priests = input.toInt()
    val blocksAvailable = 20_240_000
    val acolytes = 1111
    val shrine = build(priests, acolytes, blocksAvailable)
    return shrine.width * (shrine.blocks - blocksAvailable)
}

private fun part3(input: String): Int {
    val priests = input.toInt()
    val blocksAvailable = 202_400_000
    val acolytes = 10
    val shrine = build(priests, acolytes, blocksAvailable, part3 = true)
    return shrine.blocks - shrine.innerDimensions - blocksAvailable
}

private fun build(
    priests: Int,
    acolytes: Int,
    blocksAvailable: Int,
    part3: Boolean = false,
): Shrine = generateSequence(Shrine()) { shrine -> shrine.buildLevel(priests, acolytes, part3) }
    .takeUntil { shrine -> shrine.blocks - shrine.innerDimensions >= blocksAvailable }
    .last()


