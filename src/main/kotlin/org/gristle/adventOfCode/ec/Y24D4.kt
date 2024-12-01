package org.gristle.adventOfCode.ec

import org.gristle.adventOfCode.utilities.Stopwatch
import org.gristle.adventOfCode.utilities.getIntList
import kotlin.math.abs

fun main() {
    val (input1, input2, input3) = ecInputs(24, 4)
    val timer = Stopwatch(true)
    println("1. ${solve(input1, ::lowest)}: ${timer.lap()}ms")
    println("2. ${solve(input2, ::lowest)}: ${timer.lap()}ms")
    println("3. ${solve(input3, ::least)}: ${timer.lap()}ms")
    println("Total: ${timer.stop()}ms")
}

private fun lowest(nails: List<Int>) = nails.min()
private fun least(nails: List<Int>): Int = nails.sorted().let { it[it.size / 2] }
private fun solve(input: String, getTarget: (List<Int>) -> Int): Int {
    val nails = input.getIntList()
    val target = getTarget(nails)
    return nails.sumOf { nail -> abs(nail - target) }
}
